package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.Process
import android.view.WindowManager
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.io.File

/** Real password/Mapping UI and durable snapshots; every controller access is read-only. */
class MenuProposalUiFixture(private val ui: ComposeTestRule) {
    private var scenario: ActivityScenario<CookbookProofActivity>? = null
    private var authentication: AuthenticatedCookbookFixture? = null
    private var directory: File? = null
    private lateinit var accepted: List<MenuSnapshot>
    private lateinit var files: Map<String, String>
    private var sequence = 0
    private val auth get() = requireNotNull(authentication)
    fun open() {
        scenario = ActivityScenario.launch(CookbookProofActivity::class.java)
        requireNotNull(scenario).onActivity { directory = it.proofDirectory.canonicalFile }
        authentication = AuthenticatedCookbookFixture(ui, requireNotNull(scenario))
        auth.attach()
        auth.setupCacheAndCook()
        accepted = snapshots(); files = auth.hashes()
        assertTrue("Genuine baseline contains one actual confirmed Menu and encrypted jars", accepted.size == 1 && files.size == 2)
        preserved(); report("proposal-baseline-authenticated")
    }
    private fun own() {
        assertTrue("Every UI action/read stays in actual secure authenticated own foreground", auth.foreground() && timerMain {
            auth.activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0 &&
                CookbookUiDiagnostics.stateFlags(auth.activity)["vaultReady"] == true
        })
    }
    fun snapshots(): List<MenuSnapshot> = timerMain { auth.book().sources.snapshots.toList() }
    fun done() { ui.waitUntil(10_000) { timerMain { !auth.book().sources.busy } }; own() }
    fun field(tag: String, value: String) {
        own(); ui.onNodeWithTag(tag).performScrollTo().performTextReplacement(value); ui.waitForIdle()
    }
    fun editable(tag: String): String {
        own(); return ui.onNodeWithTag(tag).fetchSemanticsNode().config[SemanticsProperties.EditableText].text
    }
    fun role(role: ColumnRole, column: String) = field("column-${role.name}", column)
    fun firstRow(value: String) { own(); auth.fill("First data row", value) }
    fun repreview() { own(); auth.click("Preview columns"); done() }
    fun preview(table: String): MenuOrigin {
        own(); auth.click("Sources")
        val id = "proposal_${auth.directory.name.removePrefix("cookbook-ui-proof-")}_${++sequence}"
        val origin = MenuOrigin(id, 0, "Proposal $sequence")
        assertTrue("Proposal always uses a new origin, never existing-source refresh", accepted.none { it.mapping.origin.key == origin.key })
        field("source-id", origin.menuId); own(); auth.fill("Local tab label", origin.title)
        field("source-table", table); own(); auth.click("Preview columns"); done()
        return origin
    }
    fun proposal(origin: MenuOrigin, recipeColumn: String) {
        assertEquals("CONFIRM", timerMain { auth.book().sources.status })
        assertTrue("Actual draft retains the exact selected new origin", timerMain { auth.book().sources.draft?.origin == origin })
        assertEquals("Actual Recipe field matches the proposal policy", recipeColumn, editable("column-RECIPE"))
        ui.onNodeWithTag("confirm-columns").performScrollTo().assertIsDisplayed()
        preserved()
    }
    fun confirm() { own(); ui.onNodeWithTag("confirm-columns").performScrollTo().performClick(); done() }
    fun status(expected: String) {
        done(); assertEquals("Real completed source status", expected, timerMain { auth.book().sources.status })
        val message = when (expected) {
            "INVALID_MAPPING" -> "Choose distinct valid columns and a data row."
            "INVALID_TABLE" -> "Table is invalid or exceeds local limits."
            "CACHE_FAILED" -> "Could not save local cache; prior data preserved."
            else -> null
        }
        message?.let { ui.onNodeWithText(it).assertExists() }
    }
    fun preserved() {
        own(); assertEquals("Accepted immutable RAM snapshots are preserved", accepted, snapshots())
        assertEquals("All accepted encrypted Cookbook/cache file bytes are preserved", files, auth.hashes())
        val inventory = MenuSnapshotStore(File(auth.directory, "menus")).inventory()
        assertEquals("No corrupt cache was created", 0, inventory.corruptFiles)
        assertEquals("Actual disk inventory equals accepted RAM", accepted.sortedBy { it.mapping.origin.key },
            inventory.snapshots.sortedBy { it.mapping.origin.key })
    }
    fun accept(origin: MenuOrigin, columns: Map<ColumnRole, Int>, row: Int, header: List<String>?, entries: List<MenuEntry>) {
        status("SYNCED")
        val saved = snapshots().single { it.mapping.origin == origin }
        val expected = MenuSnapshot(mapping = MenuMapping(origin, columns, row, header), entries = entries, refreshedAt = saved.refreshedAt)
        assertTrue("Actual successful confirmation records a real freshness timestamp", saved.refreshedAt > 0)
        assertEquals("Confirmed mapping and every literal cell retain their exact origin/row", expected, saved)
        assertEquals("Only the explicitly confirmed new Menu joins prior RAM", accepted + expected, snapshots())
        val current = auth.hashes()
        assertTrue("New cache never rewrites prior jars or Menu files", files.all { (path, digest) -> current[path] == digest })
        assertEquals("Exactly one durable cache file was added", files.size + 1, current.size)
        assertEquals("Actual durable reload equals the literal expected Menu", expected,
            MenuSnapshotStore(File(auth.directory, "menus")).load(origin))
        accepted = accepted + expected; files = current; preserved()
    }
    fun singleColumn(origin: MenuOrigin) {
        proposal(origin, ""); firstRow("1"); role(ColumnRole.RECIPE, "1")
        for (role in ColumnRole.entries.filter { it != ColumnRole.RECIPE })
            assertEquals("One-cell preview has no inferred optional role", "", editable("column-${role.name}"))
        confirm()
        accept(origin, mapOf(ColumnRole.RECIPE to 0), 1, null, listOf(MenuEntry(origin, 1, "abcd#1", "Row 1")))
    }
    fun report(phase: String) {
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefProposalPhase", phase); putString("passchefProposalFixture", auth.directory.name)
            putInt("passchefProposalPid", Process.myPid())
            putInt("passchefProposalSourceCount", snapshots().size)
            putString("passchefProposalSourceStatus", timerMain { auth.book().sources.status })
        })
    }
    fun close(primary: Throwable?) {
        val failures = mutableListOf<Throwable>()
        fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
        clean { scenario?.close() }
        val owned = directory
        if (owned != null) {
            var aliases = emptyList<String>()
            clean { VaultRepository(AtomicRecordStore(File(owned, "cookbook-v1.json"))).record()?.let {
                aliases = (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty).distinct()
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean {
                val base = InstrumentationRegistry.getInstrumentation().targetContext.noBackupFilesDir.canonicalFile
                require(owned.canonicalFile == owned && owned.parentFile == base && owned.name.startsWith("cookbook-ui-proof-"))
                val id = owned.name.removePrefix("cookbook-ui-proof-")
                require(java.util.UUID.fromString(id).toString() == id)
                assertTrue("Only the exact owned proposal fixture is removed", !owned.exists() || owned.deleteRecursively())
                assertTrue("Proposal fixture absence is verified", !owned.exists())
            }
        }
        if (failures.isNotEmpty()) {
            if (primary != null) failures.forEach(primary::addSuppressed)
            else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
        }
    }
}

package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.recipe.TagCodec
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

/** Actual UI mapping/refusal with genuine synthetic jars; no Google or global clipboard operation. */
@RunWith(AndroidJUnit4::class)
class MenuMappingPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private lateinit var state: ColdCacheObservation
    private var phase = "mapping-arguments"
    private fun report(name: String, failed: Boolean = false) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefMappingPhase", name)
            putString("passchefMappingFixtureId", state.activity.proofId)
            putBoolean("passchefMappingFailed", failed)
            state.flags().forEach { (key, value) -> putBoolean("passchefMapping_$key", value) }
        })
    }
    private fun click(text: String) {
        state.requireReady()
        ui.onNode(hasClickAction() and hasText(text)).scrollIfAble().performClick()
    }
    private fun input(tag: String, text: String) {
        state.requireReady()
        ui.onNodeWithTag(tag).scrollIfAble().performTextReplacement(text)
    }
    private fun idle() {
        state.await { state.foreground() && timerMain { state.vault().readyForCooking() && !state.book().sources.busy } }
        ui.waitForIdle()
    }
    private fun confirm() {
        state.requireReady()
        RedesignUi.confirmIfValid(ui); idle()
    }
    private fun cook() {
        state.requireReady()
        ui.onNodeWithTag("manual-cook").scrollIfAble().performClick()
        state.await { state.foreground() && timerMain { !state.book().cooking && (state.book().hasDish() || state.book().code != null) } }
        ui.waitForIdle()
    }
    @Test fun wrongMenuTagAndIrregularColumnsKeepLastGoodCache() {
        val id = requireNotNull(InstrumentationRegistry.getArguments().getString("passchefMappingFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }; state = ColdCacheObservation(activity)
        try {
            report("owner-mapping-setup-biometric")
            val fixture = ColdCacheUiFixture(ui, state)
            fixture.setup(); fixture.prepareCache()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            val other = MenuOrigin("Other_Menu_0123456789", 0, "Other Menu")
            val table = "Label\tRecipe\tRecipe\t\tUsername\nOther\tabcd#1\twxyz#1\t\t00123\n" +
                "Ragged\tabcd#1\nShort\n\t\t\t\t\nInvalid\tnot a Recipe\t\t\tignored\n" +
                "Unicode\txy@1\t\t\tユーザー\n\txyz#1\talt#1\t\t"
            report("irregular-mapping-preview")
            RedesignUi.offlineDraft(ui, { state.book() }, other, table); idle()
            ui.onNodeWithTag("confirm-columns").assertExists()
            val draft = timerMain { requireNotNull(state.book().sources.draft) }
            assertTrue("Actual preview retains duplicate/blank headers and ragged physical rows", draft.origin == other &&
                draft.rows.first() == listOf("Label", "Recipe", "Recipe", "", "Username") &&
                draft.rows[2] == listOf("Ragged", "abcd#1") && draft.rows[3] == listOf("Short"))
            fun unchanged() {
                assertTrue("Rejected/unconfirmed mapping preserves last-good bytes and RAM", baseline.matches(activity.proofDirectory) &&
                    timerMain { state.book().sources.snapshots == listOf(coldExpected(baseline.refreshedAt)) })
                assertTrue("Rejected source never appears in durable inventory", MenuSnapshotStore(File(activity.proofDirectory, "menus"))
                    .inventory().let { it.corruptFiles == 0 && it.snapshots == listOf(coldExpected(baseline.refreshedAt)) })
            }
            unchanged()
            ui.onNode(hasSetTextAction() and hasText("Advanced: data starts at row")).scrollIfAble().performTextReplacement("2")
            input("column-LABEL", "2"); input("column-RECIPE", "2"); input("column-USERNAME", "5")
            input("column-URL", ""); input("column-NOTES", ""); confirm()
            assertTrue("Duplicate role columns are explicitly refused", timerMain { state.book().sources.status == "INVALID_MAPPING" })
            ui.onNodeWithText(RedesignUi.message("INVALID_MAPPING")).assertExists(); unchanged()
            report("missing-recipe-column-refusal")
            input("column-LABEL", "1"); input("column-RECIPE", ""); confirm()
            assertTrue("Missing Recipe column remains refused", timerMain { state.book().sources.status == "INVALID_MAPPING" })
            unchanged()
            input("column-RECIPE", "2"); confirm()
            val saved = timerMain { state.book().sources.snapshots.single { it.mapping.origin == other } }
            val expected = listOf(MenuEntry(other, 2, "abcd#1", "Other", "00123"),
                MenuEntry(other, 3, "abcd#1", "Ragged", ""), MenuEntry(other, 7, "xy@1", "Unicode", "ユーザー"),
                MenuEntry(other, 8, "xyz#1", "Row 8", ""))
            assertTrue("Explicit UI mapping preserves physical rows, literals and missing cells; invalid Recipes are omitted",
                saved.entries == expected && saved.mapping.columns == mapOf(ColumnRole.LABEL to 0, ColumnRole.RECIPE to 1, ColumnRole.USERNAME to 4) &&
                    saved.mapping.firstDataRow == 2 && saved.mapping.confirmedHeader == draft.rows.first() &&
                    MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(other) == saved)
            assertTrue("Confirmed different source preserves the original cache/jars", baseline.matches(activity.proofDirectory))
            report("correct-menu-tag-positive-control")
            RedesignUi.enterCode(ui); click(coldOrigin.title)
            val tag = TagCodec().compute(requireNotNull(RecipeParser.parse("abcd#1")), "aB!*", coldOrigin.menuId)
            input("manual-recipe", "abcd#1.$tag"); cook()
            ui.onNodeWithTag("dish-display").assertTextEquals("••••••••")
            ui.onNodeWithTag("dish-copy").assertIsEnabled()
            fixture.hideIme()
            ui.onNodeWithTag("dish-reveal").scrollIfAble().performClick()
            ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
            report("wrong-menu-tag-refusal")
            click(other.title)
            assertTrue("Changing Menu keeps the exact same tagged Recipe input", ui.onNodeWithTag("manual-recipe")
                .fetchSemanticsNode().config[SemanticsProperties.EditableText].text == "abcd#1.$tag")
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            cook()
            ui.onNodeWithTag("cook-error").assertTextEquals(RedesignUi.message("RECIPE_MISMATCH"))
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            assertTrue("A valid tagged Recipe bound to another Menu cannot retain any old Dish", timerMain {
                state.book().code == "RECIPE_MISMATCH" && !state.book().hasDish() && !state.book().revealed && state.book().dish.isEmpty()
            })
            assertTrue("Wrong Menu refusal preserves both accepted cache files and jars", baseline.matches(activity.proofDirectory) &&
                MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(other) == saved)
            report("mapping-and-wrong-menu-passed")
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", failed = true) }
            throw AssertionError("Menu mapping/tag UI checkpoint failed; safe phase recorded")
        } finally { try { scenario.close() } finally { cleanupColdFixture(activity) } }
    }
}

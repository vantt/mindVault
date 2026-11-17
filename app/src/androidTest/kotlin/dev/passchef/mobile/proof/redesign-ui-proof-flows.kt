package dev.passchef.mobile.proof

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.MenuOrigin
import dev.passchef.mobile.cookbook.cookbookMessage

/**
 * Real-UI steps for the 2026-11-10 redesign (plan 261110): gate screens, 3-tab shell, connect wizard.
 * Proofs keep driving genuine Compose actions; only where the new UI no longer accepts a Sheet id for an
 * offline table (D9) is the controller's id-keeping entry point used so cache identity stays comparable.
 */
/** performScrollTo when a scrollable ancestor exists; the redesign's nav bar, header and filter chips are fixed. */
fun SemanticsNodeInteraction.scrollIfAble(): SemanticsNodeInteraction = apply {
    val scrollable = runCatching {
        var node: androidx.compose.ui.semantics.SemanticsNode? = fetchSemanticsNode().parent
        var found = false
        while (node != null && !found) { found = node.config.contains(androidx.compose.ui.semantics.SemanticsActions.ScrollBy); node = node.parent }
        found
    }.getOrDefault(false)
    if (scrollable) performScrollTo()
}

object RedesignUi {
    private fun ComposeTestRule.has(tag: String) = onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
    /** Real controller of a running MainActivity (same reflection path the existing proofs use). */
    fun book(activity: dev.passchef.mobile.MainActivity): CookbookController =
        dev.passchef.mobile.MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }.get(activity) as CookbookController
    /** Scroll only when the node lives in a scrollable parent (bottom nav / header are fixed). */
    private fun SemanticsNodeInteraction.reach(): SemanticsNodeInteraction = scrollIfAble()
    fun tap(ui: ComposeTestRule, tag: String) = ui.onNodeWithTag(tag).reach().performClick()
    fun type(ui: ComposeTestRule, tag: String, value: String) = ui.onNodeWithTag(tag).reach().performTextReplacement(value)

    /** E1–E4 explainer is skippable; it only shows on a phone that never finished it. */
    fun skipExplainer(ui: ComposeTestRule) { if (ui.has("onboarding-skip")) tap(ui, "onboarding-skip"); ui.waitForIdle() }

    /** SU1–SU3. [fingerprint] = system prompt wraps the key (owner authorizes); false = password only. */
    fun setupVault(ui: ComposeTestRule, password: String, parts: List<String> = listOf("aB!*"), fingerprint: Boolean = true) {
        ui.waitUntil(10_000) { ui.has("onboarding-skip") || ui.has("setup-master") }
        skipExplainer(ui)
        type(ui, "setup-master", password); type(ui, "setup-master-confirm", password); tap(ui, "setup-ack")
        ui.onNode(hasClickAction() and hasText("Next", substring = true)).reach().performClick()
        parts.forEachIndexed { i, part ->
            if (i > 0) tap(ui, "setup-add-spice")
            type(ui, "setup-spice-${i + 1}", part)
        }
        ui.onNode(hasClickAction() and hasText("Next", substring = true)).reach().performClick()
        tap(ui, if (fingerprint) "setup-biometric" else "setup-create")
    }

    /** UL2: master password always one tap away (fingerprint prompt may have to be dismissed by the owner). */
    fun unlockWithPassword(ui: ComposeTestRule, password: String) {
        ui.waitUntil(10_000) { ui.has("unlock-password") || ui.has("unlock-password-field") }
        if (!ui.has("unlock-password-field")) tap(ui, "unlock-password")
        type(ui, "unlock-password-field", password); tap(ui, "unlock-submit")
    }
    fun unlockWithFingerprint(ui: ComposeTestRule) { ui.waitUntil(10_000) { ui.has("unlock-biometric") }; tap(ui, "unlock-biometric") }

    fun passwords(ui: ComposeTestRule) = tap(ui, "nav-menu")
    /** Passwords tab root. Unlock restores the last route (D5), so a pushed detail is popped by reselecting the tab. */
    fun passwordsRoot(ui: ComposeTestRule) {
        tap(ui, "nav-menu"); ui.waitForIdle()
        if (ui.onAllNodes(hasContentDescription("Back") or hasContentDescription("Quay lại")).fetchSemanticsNodes().isNotEmpty()) { tap(ui, "nav-menu"); ui.waitForIdle() }
    }
    fun enterCode(ui: ComposeTestRule) = tap(ui, "nav-cook")
    fun settings(ui: ComposeTestRule) = tap(ui, "nav-kitchen")
    fun lockNow(ui: ComposeTestRule) = tap(ui, "lock-now")

    /** Settings › + Connect a sheet (wizard shows its current step, e.g. tab list while sheet metadata is held). */
    fun openConnect(ui: ComposeTestRule) { settings(ui); tap(ui, "kitchen-connect") }
    /** Settings › + Connect a sheet › Paste a link (field tag kept `source-id`); Continue opens the filtered Picker. */
    fun openPasteLink(ui: ComposeTestRule) { settings(ui); tap(ui, "kitchen-connect"); tap(ui, "connect-paste") }
    fun continuePasteLink(ui: ComposeTestRule) = ui.onNode(hasClickAction() and hasText("Continue")).reach().performClick()

    /** Settings › + Connect a sheet › Offline table, then stage the table under [origin] (id-keeping entry point). */
    fun offlineDraft(ui: ComposeTestRule, book: () -> CookbookController, origin: MenuOrigin, table: String) {
        settings(ui); tap(ui, "kitchen-connect"); tap(ui, "connect-offline")
        timerMain { book().offlineSource(origin.menuId, origin.title, table) }
        ui.waitUntil(10_000) { ui.has("confirm-columns") }
    }
    /** CM3 → CM4 → saved. */
    fun confirmAndSave(ui: ComposeTestRule) {
        tap(ui, "confirm-columns")
        ui.waitUntil(10_000) { ui.has("connect-save") }
        tap(ui, "connect-save")
    }
    /** Old "confirm" = CM3 Review then, if the mapping is accepted, CM4 Save. Refusals stay on CM3. */
    fun confirmIfValid(ui: ComposeTestRule) {
        tap(ui, "confirm-columns"); ui.waitForIdle()
        if (ui.onAllNodesWithTag("connect-save").fetchSemanticsNodes().isNotEmpty()) tap(ui, "connect-save")
    }
    /** RD1 star (Favorite moved from list rows to detail). */
    fun favoriteFromDetail(ui: ComposeTestRule) = ui.onNode(hasContentDescription("Add favorite") or hasContentDescription("Remove favorite") or
        hasContentDescription("Yêu thích") or hasContentDescription("Bỏ yêu thích")).performClick()
    /** Pops the detail and waits until it has left (navigation recomposes asynchronously), so the next row tap hits the list. */
    fun backFromDetail(ui: ComposeTestRule) {
        val back = hasContentDescription("Back") or hasContentDescription("Quay lại")
        ui.onNode(back).performClick()
        ui.waitUntil(5_000) { ui.onAllNodes(back).fetchSemanticsNodes().isEmpty() }
        ui.waitForIdle()
    }
    /**
     * Back to the list, then reopen [label]'s detail. A row tap that lands during list recomposition can be lost,
     * so it is retried (bounded) until the app reports the row [opened]; each lost tap is reported for the record.
     */
    fun reopenRow(ui: ComposeTestRule, label: String, opened: () -> Boolean) {
        backFromDetail(ui)
        repeat(3) { attempt ->
            ui.onNode(hasClickAction() and hasText(label)).scrollIfAble().performClick()
            ui.waitForIdle()
            val deadline = android.os.SystemClock.elapsedRealtime() + 5_000
            while (!opened() && android.os.SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
            if (opened()) return
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().sendStatus(2, android.os.Bundle().apply {
                putString("passchefReopenRowLostTap", "${attempt + 1}")
                putString("passchefReopenRowTree", runCatching { ui.onRoot().printToString(maxDepth = 4) }.getOrDefault("?").take(3000))
            })
        }
    }
    /** D10: language lives in Settings once unlocked (gate screens keep the EN/VI header button). */
    fun language(ui: ComposeTestRule, vietnamese: Boolean) { settings(ui); tap(ui, if (vietnamese) "kitchen-lang-vi" else "kitchen-lang-en") }
    /** Status words now come from one table; proofs assert by code, not by duplicated copy. */
    fun message(code: String, vi: Boolean = false) = cookbookMessage(code, vi)

    const val LOCKED_TITLE_EN = "Your vault is "
    const val LOCKED_TITLE_VI = "Két riêng đang "
    fun lockedTitle(vi: Boolean) = if (vi) LOCKED_TITLE_VI else LOCKED_TITLE_EN
}

package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Paired UNKNOWN/FINGER native samples; no edits to archived edge tests or production UI. */
@RunWith(AndroidJUnit4::class)
class LanguageFingerPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private var phase = "language-finger-start"
    private var unknownDown = 0; private var unknownUp = 0
    private var fingerDown = 0; private var fingerUp = 0
    private fun report(name: String, flags: Map<String, Boolean> = emptyMap()) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefLanguageFingerPhase", name)
            putInt("passchefLanguageFingerUnknownDown", unknownDown); putInt("passchefLanguageFingerUnknownUp", unknownUp)
            putInt("passchefLanguageFingerFingerDown", fingerDown); putInt("passchefLanguageFingerFingerUp", fingerUp)
            putLong("passchefLanguageFingerConfiguredDwellMillis", 60)
            flags.forEach { (key, value) -> putBoolean("passchefLanguageFinger_$key", value) }
        })
    }
    private fun locale(vietnamese: Boolean) {
        ui.onNodeWithText(if (vietnamese) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
        ui.onNode(hasSetTextAction() and hasText(if (vietnamese) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
        ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).assertIsDisplayed()
        ui.onNodeWithText(if (vietnamese) "PassChef — private Cookbook" else "PassChef — Cookbook riêng").assertDoesNotExist()
    }
    @Test fun pairedUnknownAndFingerExpandedEdgePointersSwitchOnlyForFinger() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val guards = SecurityUiObservation(activity)
        try {
            ui.waitForIdle(); guards.foreground()
            hideTimerIme(activity, guards::foreground)
            var vietnamese = ui.onAllNodes(hasClickAction() and hasText("EN")).fetchSemanticsNodes().isNotEmpty()
            for (sample in 1..2) {
                locale(vietnamese)
                var baseline: LanguagePointerPoint? = null
                for (tool in listOf(MotionEvent.TOOL_TYPE_UNKNOWN, MotionEvent.TOOL_TYPE_FINGER)) {
                    val kind = if (tool == MotionEvent.TOOL_TYPE_FINGER) "finger" else "unknown"
                    ui.waitForIdle(); guards.foreground(); locale(vietnamese)
                    val node = ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).fetchSemanticsNode()
                    val point = languagePointerPoint(activity, node)
                    assertTrue("Both variants require the same guarded expanded midpoint", point.flags.values.all { it })
                    if (baseline == null) baseline = point
                    assertTrue("Paired variants keep exact same-language geometry", point == baseline)
                    report("language-$sample-$kind-point", point.flags + mapOf("pairedGeometryUnchanged" to true))
                    val downAt = SystemClock.uptimeMillis()
                    for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
                        guards.foreground()
                        val built = languagePointerEvent(point, downAt, action, tool)
                        try {
                            InstrumentationRegistry.getInstrumentation().sendPointerSync(built.event)
                            assertTrue("Injected event retains requested tool kind", built.event.getToolType(0) == tool)
                            if (tool == MotionEvent.TOOL_TYPE_FINGER) {
                                if (action == MotionEvent.ACTION_DOWN) fingerDown++ else fingerUp++
                            } else if (action == MotionEvent.ACTION_DOWN) unknownDown++ else unknownUp++
                        } finally { built.event.recycle() }
                        report("language-$sample-$kind-event-$action", built.flags)
                        if (action == MotionEvent.ACTION_DOWN) Thread.sleep(60)
                    }
                    ui.waitForIdle(); guards.foreground()
                    if (tool == MotionEvent.TOOL_TYPE_FINGER) vietnamese = !vietnamese
                    locale(vietnamese)
                    report("language-$sample-$kind-observed", mapOf("expectedLocalizedUi" to true,
                        "fingerSwitched" to (tool == MotionEvent.TOOL_TYPE_FINGER),
                        "unknownRetained" to (tool == MotionEvent.TOOL_TYPE_UNKNOWN)))
                }
            }
            assertTrue("Two UNKNOWN and two FINGER native pairs completed", unknownDown == 2 && unknownUp == 2 && fingerDown == 2 && fingerUp == 2)
            guards.foreground(); report("language-finger-passed", mapOf("passed" to true))
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", mapOf("failed" to true)) }
            throw AssertionError("Language pointer-kind acceptance failed; safe metadata recorded")
        } finally { ui.activityRule.scenario.close(); directory.deleteRecursively() }
    }
}

package dev.passchef.mobile.proof

import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Two real expanded-edge pointer samples only; no whole-target/layout/TalkBack acceptance claim. */
@RunWith(AndroidJUnit4::class)
class LanguageEdgePhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private var phase = "language-edge-start"
    private var downCount = 0
    private var upCount = 0
    private var edgeDwellMillis = 0L
    private data class Edge(val x: Float, val y: Float, val flags: Map<String, Boolean>, val sizes: Map<String, Int>)
    private fun report(name: String, flags: Map<String, Boolean> = emptyMap(), sizes: Map<String, Int> = emptyMap()) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefLanguageEdgePhase", name)
            putInt("passchefLanguageEdgeDownCount", downCount); putInt("passchefLanguageEdgeUpCount", upCount)
            putLong("passchefLanguageEdgeOptionalEdgeDwellMillis", edgeDwellMillis)
            flags.forEach { (key, value) -> putBoolean("passchefLanguageEdge_$key", value) }
            sizes.forEach { (key, value) -> putInt("passchefLanguageEdge_$key", value) }
        })
    }
    private fun edge(activity: CookbookProofActivity, node: SemanticsNode): Edge = timerMain {
        val visual = node.boundsInWindow
        val root = node.boundsInRoot
        val touch = node.touchBoundsInRoot
        val dx = visual.left - root.left; val dy = visual.top - root.top
        val left = touch.left + dx; val top = touch.top + dy
        val right = touch.right + dx; val bottom = touch.bottom + dy
        val windowX = (left + right) / 2
        val windowY = when {
            bottom - visual.bottom > 2 -> (visual.bottom + bottom) / 2
            visual.top - top > 2 -> (top + visual.top) / 2
            else -> Float.NaN
        }
        val decor = activity.window.decorView
        val offset = IntArray(2).also(decor::getLocationOnScreen)
        val x = windowX + offset[0]; val y = windowY + offset[1]
        val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
        val insets = ViewCompat.getRootWindowInsets(decor)
        val density = activity.resources.displayMetrics.density
        val flags = mapOf("pointFinite" to listOf(x, y, dx, dy, left, top, right, bottom, density).all { it.isFinite() },
            "visualPlaced" to (visual.width > 0 && visual.height > 0),
            "touch48dp" to (density > 0 && touch.width >= 48 * density && touch.height >= 48 * density),
            "insideExpandedTouch" to (windowX > left && windowX < right && windowY > top && windowY < bottom),
            "outsideRawLayout" to (windowX < visual.left || windowX > visual.right || windowY < visual.top || windowY > visual.bottom),
            "clearEdgeMidpoint" to (bottom - visual.bottom > 2 || visual.top - top > 2),
            "insideOwnWindow" to (x >= offset[0] && y >= offset[1] && x < offset[0] + decor.width && y < offset[1] + decor.height),
            "insideVisibleFrame" to (x >= visible.left && y >= visible.top && x < visible.right && y < visible.bottom),
            "insetsAvailable" to (insets != null), "imeHidden" to (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false))
        Edge(x, y, flags, mapOf("rawWidthDpX1000" to (visual.width / density * 1000).toInt(),
            "rawHeightDpX1000" to (visual.height / density * 1000).toInt(),
            "touchWidthDpX1000" to (touch.width / density * 1000).toInt(),
            "touchHeightDpX1000" to (touch.height / density * 1000).toInt()))
    }
    private fun afterInputDiagnostics(guards: SecurityUiObservation, name: String) {
        val english = runCatching {
            ui.onAllNodesWithText("PassChef — private Cookbook").fetchSemanticsNodes().isNotEmpty()
        }
        val vietnamese = runCatching {
            ui.onAllNodesWithText("PassChef — Cookbook riêng").fetchSemanticsNodes().isNotEmpty()
        }
        report(name, mapOf("actualHeadlineEnglish" to english.getOrDefault(false), "actualHeadlineVietnamese" to vietnamese.getOrDefault(false),
            "headlineReadsSucceeded" to (english.isSuccess && vietnamese.isSuccess),
            "afterInputForegroundGuardSucceeded" to runCatching { guards.foreground() }.isSuccess))
    }
    @Test fun realLanguageCenterPointersWithPhysicalDwellSwitchBothLocalizedLockedScreens() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val guards = SecurityUiObservation(activity)
        try {
            ui.waitForIdle(); guards.foreground()
            hideTimerIme(activity, guards::foreground)
            var vietnamese = ui.onAllNodes(hasClickAction() and hasText("EN")).fetchSemanticsNodes().isNotEmpty()
            for (sample in 1..2) {
                val label = if (vietnamese) "EN" else "VI"
                ui.onNodeWithText(if (vietnamese) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
                ui.onNode(hasSetTextAction() and hasText(if (vietnamese) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
                ui.waitForIdle(); guards.foreground()
                val node = ui.onNode(hasClickAction() and hasText(label)).fetchSemanticsNode()
                val point = timerWindowPoint(activity, node)
                report("language-center-$sample-point", point.checks)
                assertTrue("Actual center control requires finite placed visible own-window point and hidden IME", point.checks.values.all { it })
                val downAt = SystemClock.uptimeMillis()
                for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
                    guards.foreground()
                    val event = MotionEvent.obtain(downAt, SystemClock.uptimeMillis(), action, point.x, point.y, 0)
                    event.source = InputDevice.SOURCE_TOUCHSCREEN
                    try {
                        InstrumentationRegistry.getInstrumentation().sendPointerSync(event)
                        if (action == MotionEvent.ACTION_DOWN) downCount++ else upCount++
                    } finally { event.recycle() }
                    report("language-center-$sample-event-$action", point.checks)
                    if (action == MotionEvent.ACTION_DOWN) Thread.sleep(60) // Ordinary physical dwell; production deadlines unchanged.
                }
                ui.waitForIdle(); afterInputDiagnostics(guards, "language-center-$sample-after-input"); guards.foreground()
                vietnamese = !vietnamese
                ui.onNodeWithText(if (vietnamese) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
                ui.onNode(hasSetTextAction() and hasText(if (vietnamese) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
                ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).assertIsDisplayed()
                report("language-center-$sample-switched", mapOf("localizedHeadlineChanged" to true, "localizedMasterChanged" to true))
            }
            assertTrue("Both real center-control DOWN/UP samples completed", downCount == 2 && upCount == 2)
            assertFalse("Locked center-control fixture creates no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
            guards.foreground(); report("language-center-passed", mapOf("passed" to true))
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", mapOf("failed" to true)) }
            throw AssertionError("Language center-control acceptance failed; safe metadata recorded")
        } finally { ui.activityRule.scenario.close(); directory.deleteRecursively() }
    }
    @Test fun realExpandedLanguageEdgesWithPhysicalDwellSwitchBothLocalizedLockedScreens() {
        edgeDwellMillis = 60
        realExpandedLanguageEdgePointersSwitchBothLocalizedLockedScreens()
    }
    @Test fun realExpandedLanguageEdgePointersSwitchBothLocalizedLockedScreens() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val guards = SecurityUiObservation(activity)
        try {
            ui.waitForIdle(); guards.foreground()
            hideTimerIme(activity, guards::foreground)
            var vietnamese = ui.onAllNodes(hasClickAction() and hasText("EN")).fetchSemanticsNodes().isNotEmpty()
            for (sample in 1..2) {
                val label = if (vietnamese) "EN" else "VI"
                ui.onNodeWithText(if (vietnamese) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
                ui.onNode(hasSetTextAction() and hasText(if (vietnamese) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
                ui.waitForIdle(); guards.foreground()
                val node = ui.onNode(hasClickAction() and hasText(label)).fetchSemanticsNode()
                val point = edge(activity, node)
                report("language-edge-$sample-point", point.flags, point.sizes)
                assertTrue("Real input point must lie inside expanded48dp touch bounds and outside raw layout", point.flags.values.all { it })
                val downAt = SystemClock.uptimeMillis()
                for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
                    guards.foreground()
                    val event = MotionEvent.obtain(downAt, SystemClock.uptimeMillis(), action, point.x, point.y, 0)
                    event.source = InputDevice.SOURCE_TOUCHSCREEN
                    try {
                        InstrumentationRegistry.getInstrumentation().sendPointerSync(event)
                        if (action == MotionEvent.ACTION_DOWN) downCount++ else upCount++
                    } finally { event.recycle() }
                    report("language-edge-$sample-event-$action", point.flags, point.sizes)
                    if (action == MotionEvent.ACTION_DOWN && edgeDwellMillis > 0) Thread.sleep(edgeDwellMillis)
                }
                ui.waitForIdle(); afterInputDiagnostics(guards, "language-edge-$sample-after-input"); guards.foreground()
                vietnamese = !vietnamese
                ui.onNodeWithText(if (vietnamese) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
                ui.onNode(hasSetTextAction() and hasText(if (vietnamese) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
                ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).assertIsDisplayed()
                report("language-edge-$sample-switched", mapOf("localizedHeadlineChanged" to true, "localizedMasterChanged" to true))
            }
            assertTrue("Both actual expanded-edge DOWN/UP samples completed", downCount == 2 && upCount == 2)
            assertFalse("Locked edge fixture creates no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
            guards.foreground(); report("language-edge-passed", mapOf("passed" to true))
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", mapOf("failed" to true)) }
            throw AssertionError("Language edge acceptance failed; safe flags and sizes recorded")
        } finally { ui.activityRule.scenario.close(); directory.deleteRecursively() }
    }
}

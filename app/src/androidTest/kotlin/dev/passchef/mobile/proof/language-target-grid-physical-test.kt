package dev.passchef.mobile.proof

import android.graphics.Rect
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.accessibility.AccessibilityManager
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Nine interior points of the declared language target; no every-pixel/control/font-matrix claim. */
@RunWith(AndroidJUnit4::class)
class LanguageTargetGridPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private lateinit var activity: CookbookProofActivity
    private lateinit var guards: SecurityUiObservation
    private var phase = "grid-start"
    private var fixtureId = ""
    private var downs = 0; private var ups = 0; private var observed = 0
    private fun report(name: String, flags: Map<String, Boolean> = emptyMap(), numbers: Map<String, Int> = emptyMap()) {
        phase = name
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefLanguageGridPhase", name)
            putString("passchefLanguageGridFixtureId", fixtureId)
            putInt("passchefLanguageGridPid", Process.myPid())
            putInt("passchefLanguageGridDowns", downs); putInt("passchefLanguageGridUps", ups)
            putInt("passchefLanguageGridObservedToggles", observed)
            putInt("passchefLanguageGridConfiguredDwellMillis", 60)
            flags.forEach { (key, value) -> putBoolean("passchefLanguageGrid_$key", value) }
            numbers.forEach { (key, value) -> putInt("passchefLanguageGrid_$key", value) }
        })
    }
    private fun locale(vi: Boolean) {
        ui.onNodeWithText(if (vi) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
        ui.onNode(hasSetTextAction() and hasText(if (vi) "Mật khẩu chính" else "Master password")).assertIsDisplayed()
        ui.onNode(hasClickAction() and hasText(if (vi) "EN" else "VI")).assertIsEnabled().assertIsDisplayed()
        ui.onNodeWithText(if (vi) "PassChef — private Cookbook" else "PassChef — Cookbook riêng").assertDoesNotExist()
    }
    private fun lockedAndDirectTouch() {
        val state = timerMain { CookbookUiDiagnostics.stateFlags(activity) }
        assertTrue("Language-only fixture retains no ready session or Dish",
            listOf("vaultUnlocked", "vaultReady", "readyDish", "lifetimePresent", "hasDish", "dishNonempty")
                .all { state[it] == false })
        assertTrue("Every FINGER pair requires touch exploration off", timerMain {
            !(activity.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).isTouchExplorationEnabled
        })
    }
    private fun point(vi: Boolean, column: Int, row: Int, index: Int): LanguagePointerPoint {
        val matcher = hasClickAction() and hasText(if (vi) "EN" else "VI")
        ui.onNode(matcher).assertIsEnabled().assertIsDisplayed()
        val nodes = ui.onAllNodes(matcher).fetchSemanticsNodes()
        assertTrue("Exactly one current language target is required", nodes.size == 1)
        return timerMain {
            val node = nodes.single()
            val visual = node.boundsInWindow; val root = node.boundsInRoot; val touch = node.touchBoundsInRoot
            val dx = visual.left - root.left; val dy = visual.top - root.top
            val left = touch.left + dx; val top = touch.top + dy
            val right = touch.right + dx; val bottom = touch.bottom + dy
            val density = activity.resources.displayMetrics.density
            val windowX = listOf(left + density, (left + right) / 2, right - density)[column]
            val windowY = listOf(top + density, (top + bottom) / 2, bottom - density)[row]
            val decor = activity.window.decorView
            val offset = IntArray(2).also(decor::getLocationOnScreen)
            val x = windowX + offset[0]; val y = windowY + offset[1]
            val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
            val insets = ViewCompat.getRootWindowInsets(decor)
            val flags = mapOf(
                "finite" to listOf(x, y, dx, dy, left, top, right, bottom, density).all { it.isFinite() },
                "placed" to (node.layoutInfo.isAttached && node.layoutInfo.isPlaced && visual.width > 0 && visual.height > 0),
                "declaredTouch48dp" to (density > 0 && touch.width >= 48 * density && touch.height >= 48 * density),
                "insideDeclaredTouch" to (windowX > left && windowX < right && windowY > top && windowY < bottom),
                "wholeTouchInsideVisible" to (left + offset[0] >= visible.left && top + offset[1] >= visible.top &&
                    right + offset[0] <= visible.right && bottom + offset[1] <= visible.bottom),
                "wholeTouchInsideOwnWindow" to (left >= 0 && top >= 0 && right <= decor.width && bottom <= decor.height),
                "pointInsideVisible" to (x >= visible.left && y >= visible.top && x < visible.right && y < visible.bottom),
                "insetsAvailable" to (insets != null), "imeHidden" to (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false))
            val configuration = activity.resources.configuration
            report("grid-$index-${if (vi) "vi" else "en"}-geometry", flags, mapOf(
                "pointIndex" to index, "column" to column, "row" to row,
                "fontScaleX1000" to (configuration.fontScale * 1000).toInt(), "orientation" to configuration.orientation,
                "densityX1000" to (density * 1000).toInt(), "windowWidthPx" to decor.width, "windowHeightPx" to decor.height,
                "touchWidthDpX1000" to (touch.width / density * 1000).toInt(),
                "touchHeightDpX1000" to (touch.height / density * 1000).toInt()))
            assertTrue("Fresh 1dp-inset grid point requires visible declared48dp touch target and hidden IME", flags.values.all { it })
            LanguagePointerPoint(x, y, visual, touch, flags)
        }
    }
    private fun tap(vi: Boolean, column: Int, row: Int, index: Int) {
        ui.waitForIdle(); guards.foreground(); lockedAndDirectTouch(); locale(vi)
        val target = point(vi, column, row, index)
        val downAt = SystemClock.uptimeMillis()
        for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
            guards.foreground()
            val built = languagePointerEvent(target, downAt, action, MotionEvent.TOOL_TYPE_FINGER)
            try {
                val event = built.event
                assertTrue("Only a real touchscreen FINGER event with unchanged fields is dispatched", built.flags.values.all { it } &&
                    event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER && event.source == InputDevice.SOURCE_TOUCHSCREEN &&
                    event.pointerCount == 1 && event.actionMasked == action)
                assertTrue("UP retains the original downTime and actual60ms dwell", event.downTime == downAt &&
                    (action != MotionEvent.ACTION_UP || event.eventTime - downAt >= 60))
                instrumentation.sendPointerSync(event)
                assertTrue("Dispatched pointer retains actual FINGER/source readback", event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER &&
                    event.source == InputDevice.SOURCE_TOUCHSCREEN)
                if (action == MotionEvent.ACTION_DOWN) downs++ else ups++
                report("grid-$index-event-$action", built.flags,
                    mapOf("actualEventTimeFromDownMillis" to (event.eventTime - downAt).toInt()))
            } finally { built.event.recycle() }
            if (action == MotionEvent.ACTION_DOWN) Thread.sleep(60)
        }
        ui.waitForIdle(); guards.foreground(); locale(!vi)
        observed++
        report("grid-$index-${if (vi) "en" else "vi"}-observed", mapOf("localizedHeadlineAndPassword" to true))
    }
    @Test fun nineInteriorLanguageTargetPointsSwitchEnglishVietnameseAndBackWithFinger() {
        var scenario: ActivityScenario<CookbookProofActivity>? = null
        var directory: File? = null
        var primary: Throwable? = null
        val parent = instrumentation.targetContext.noBackupFilesDir.canonicalFile
        fun owned(file: File) {
            val id = file.name.removePrefix("cookbook-ui-proof-")
            assertTrue("Cleanup is restricted to the recorded canonical UUID", file.parentFile == parent &&
                file.canonicalFile == file && file.name == "cookbook-ui-proof-$id" && UUID.fromString(id).toString() == id)
        }
        try {
            val launched = ActivityScenario.launch(CookbookProofActivity::class.java)
            scenario = launched
            launched.onActivity { activity = it }
            val file = activity.proofDirectory.canonicalFile
            owned(file); directory = file; fixtureId = file.name.removePrefix("cookbook-ui-proof-")
            guards = SecurityUiObservation(activity)
            ui.waitForIdle(); guards.foreground(); hideTimerIme(activity, guards::foreground)
            lockedAndDirectTouch()
            val needsEnglish = ui.onAllNodes(hasClickAction() and hasText("EN")).fetchSemanticsNodes().isNotEmpty()
            locale(needsEnglish)
            if (needsEnglish) ui.onNode(hasClickAction() and hasText("EN")).performClick()
            ui.waitForIdle(); guards.foreground(); locale(false)
            report("grid-prepared-english", mapOf("actualInitialVietnamese" to needsEnglish,
                "actualInitialEnglish" to !needsEnglish, "semanticEnglishNormalizationOnly" to needsEnglish,
                "touchExplorationOff" to true))
            for (row in 0..2) for (column in 0..2) {
                val index = row * 3 + column
                tap(false, column, row, index); tap(true, column, row, index)
            }
            assertTrue("Exactly nine EN→VI→EN FINGER grid pairs completed", downs == 18 && ups == 18 && observed == 18)
            assertTrue("Locked target test creates no encrypted Cookbook", !File(file, "cookbook-v1.json").exists())
            lockedAndDirectTouch()
            report("grid-nine-points-passed", mapOf("passed" to true, "lockedNoDish" to true))
        } catch (failure: Throwable) {
            primary = failure
            runCatching { report("$phase-failed", mapOf("failed" to true)) }
            throw failure
        } finally {
            val failures = mutableListOf<Throwable>()
            fun clean(work: () -> Unit) { try { work() } catch (failure: Throwable) { failures += failure } }
            clean { scenario?.close() }
            clean { directory?.let { owned(it); assertTrue("Exact owned UUID removal succeeds", !it.exists() || it.deleteRecursively()) } }
            clean { directory?.let { owned(it); assertTrue("Exact owned UUID removal is verified", !it.exists()) } }
            if (failures.isNotEmpty()) {
                val original = primary
                if (original != null) failures.forEach(original::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}

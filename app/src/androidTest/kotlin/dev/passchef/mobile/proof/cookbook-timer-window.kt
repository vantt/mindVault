package dev.passchef.mobile.proof

import android.graphics.Rect
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.semantics.SemanticsNode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue

fun <T> timerMain(action: () -> T): T {
    var result: Result<T>? = null
    InstrumentationRegistry.getInstrumentation().runOnMainSync { result = runCatching(action) }
    return requireNotNull(result).getOrThrow()
}

/** Normal own-window IME request; no global injection or production clock/session change. */
fun hideTimerIme(activity: CookbookProofActivity, foreground: () -> Unit) {
    timerMain { activity.getSystemService(InputMethodManager::class.java)
        .hideSoftInputFromWindow(activity.window.decorView.windowToken, 0) }
    fun hidden() = timerMain { ViewCompat.getRootWindowInsets(activity.window.decorView)?.let {
        !it.isVisible(WindowInsetsCompat.Type.ime())
    } == true }
    val deadline = SystemClock.elapsedRealtime() + 10_000
    while (!hidden() && SystemClock.elapsedRealtime() < deadline) { foreground(); Thread.sleep(100) }
    foreground()
    assertTrue("Actual own-window IME is hidden before timer pointer", hidden())
}

data class TimerWindowPoint(val x: Float, val y: Float, val checks: Map<String, Boolean>)
/** Fresh placed semantics coordinates and actual visible own window; coordinates never emitted. */
fun timerWindowPoint(activity: CookbookProofActivity, node: SemanticsNode): TimerWindowPoint = timerMain {
    val decor = activity.window.decorView
    val bounds = node.boundsInWindow
    val offset = IntArray(2).also(decor::getLocationOnScreen)
    val x = bounds.center.x + offset[0]
    val y = bounds.center.y + offset[1]
    val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
    val insets = ViewCompat.getRootWindowInsets(decor)
    TimerWindowPoint(x, y, mapOf("pointerFinite" to (x.isFinite() && y.isFinite()),
        "pointerNodePlaced" to (bounds.width > 0 && bounds.height > 0),
        "pointerInsideOwnWindow" to (x >= offset[0] && x < offset[0] + decor.width && y >= offset[1] && y < offset[1] + decor.height),
        "pointerInsideVisibleFrame" to visible.contains(x.toInt(), y.toInt()),
        "pointerInsetsAvailable" to (insets != null),
        "pointerImeHidden" to (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false)))
}

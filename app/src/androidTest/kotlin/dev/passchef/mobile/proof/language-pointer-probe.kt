package dev.passchef.mobile.proof

import android.graphics.Rect
import android.os.SystemClock
import android.os.Build
import android.view.InputDevice
import android.view.MotionEvent
import androidx.compose.ui.semantics.SemanticsNode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.junit.Assert.assertTrue

data class LanguagePointerPoint(val x: Float, val y: Float,
    val visual: androidx.compose.ui.geometry.Rect, val touch: androidx.compose.ui.geometry.Rect,
    val flags: Map<String, Boolean>)

/** Same bottom-first expanded midpoint as the archived edge fixture; no coordinates emitted. */
fun languagePointerPoint(activity: CookbookProofActivity, node: SemanticsNode): LanguagePointerPoint = timerMain {
    val visual = node.boundsInWindow; val root = node.boundsInRoot; val touch = node.touchBoundsInRoot
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
    LanguagePointerPoint(x, y, visual, touch, mapOf(
        "pointFinite" to listOf(x, y, dx, dy, left, top, right, bottom, density).all { it.isFinite() },
        "visualPlaced" to (visual.width > 0 && visual.height > 0),
        "touch48dp" to (density > 0 && touch.width >= 48 * density && touch.height >= 48 * density),
        "insideExpandedTouch" to (windowX > left && windowX < right && windowY > top && windowY < bottom),
        "outsideRawLayout" to (windowX < visual.left || windowX > visual.right || windowY < visual.top || windowY > visual.bottom),
        "clearEdgeMidpoint" to (bottom - visual.bottom > 2 || visual.top - top > 2),
        "insideOwnWindow" to (x >= offset[0] && y >= offset[1] && x < offset[0] + decor.width && y < offset[1] + decor.height),
        "insideVisibleFrame" to (x >= visible.left && y >= visible.top && x < visible.right && y < visible.bottom),
        "insetsAvailable" to (insets != null), "imeHidden" to (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false)))
}

data class LanguagePointerEvent(val event: MotionEvent, val flags: Map<String, Boolean>)

/** Observe the legacy constructor, then reconstruct both variants with identical non-tool fields. */
fun languagePointerEvent(point: LanguagePointerPoint, downAt: Long, action: Int, tool: Int): LanguagePointerEvent {
    require(tool == MotionEvent.TOOL_TYPE_UNKNOWN || tool == MotionEvent.TOOL_TYPE_FINGER)
    val legacy = MotionEvent.obtain(downAt, SystemClock.uptimeMillis(), action, point.x, point.y, 0)
    legacy.source = InputDevice.SOURCE_TOUCHSCREEN
    try {
        val legacyUnknown = legacy.pointerCount == 1 && legacy.actionIndex == 0 && legacy.getToolType(0) == MotionEvent.TOOL_TYPE_UNKNOWN
        assertTrue("Observe actual legacy constructor's single unknown pointer before reconstruction", legacyUnknown)
        val properties = MotionEvent.PointerProperties().also { legacy.getPointerProperties(0, it) }
        val coordinates = MotionEvent.PointerCoords().also { legacy.getPointerCoords(0, it) }
        properties.toolType = tool
        val event = MotionEvent.obtain(legacy.downTime, legacy.eventTime, legacy.action, 1, arrayOf(properties), arrayOf(coordinates),
            legacy.metaState, legacy.buttonState, legacy.xPrecision, legacy.yPrecision, legacy.deviceId,
            legacy.edgeFlags, legacy.source, legacy.flags)
        try {
            val copiedProperties = MotionEvent.PointerProperties().also { event.getPointerProperties(0, it) }
            val copiedCoordinates = MotionEvent.PointerCoords().also { event.getPointerCoords(0, it) }
            val same = event.pointerCount == legacy.pointerCount && event.actionIndex == legacy.actionIndex &&
                copiedProperties.id == properties.id && event.getPointerId(0) == legacy.getPointerId(0) &&
                event.action == legacy.action && event.actionMasked == legacy.actionMasked && event.actionButton == legacy.actionButton && event.downTime == legacy.downTime &&
                event.eventTime == legacy.eventTime && event.eventTime >= event.downTime && event.metaState == legacy.metaState &&
                event.buttonState == legacy.buttonState && event.xPrecision == legacy.xPrecision && event.yPrecision == legacy.yPrecision &&
                event.deviceId == legacy.deviceId && event.edgeFlags == legacy.edgeFlags && event.source == legacy.source &&
                event.flags == legacy.flags && event.historySize == legacy.historySize && event.rawX == legacy.rawX && event.rawY == legacy.rawY &&
                (Build.VERSION.SDK_INT < 29 || event.classification == legacy.classification) &&
                (0..63).all { copiedCoordinates.getAxisValue(it) == coordinates.getAxisValue(it) }
            val flags = mapOf("legacyConstructorUnknown" to legacyUnknown, "nonToolFieldsMatchLegacy" to same,
                "requestedToolReadback" to (copiedProperties.toolType == tool && event.getToolType(0) == tool),
                "sourceTouchscreen" to event.isFromSource(InputDevice.SOURCE_TOUCHSCREEN))
            assertTrue("Only pointer tool type differs from the actual legacy event", flags.values.all { it })
            return LanguagePointerEvent(event, flags)
        } catch (failure: Throwable) { event.recycle(); throw failure }
    } finally { legacy.recycle() }
}

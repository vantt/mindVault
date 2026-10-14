package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.WindowManager
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import java.io.File

/** Read-only observations of a locked, UUID-owned real Activity. Never stores pixels or trees. */
class SecurityUiObservation(private val activity: CookbookProofActivity) {
    var phase = "locked-security-start"
        private set
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val automation = instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
    private val file = File(activity.proofDirectory, "cookbook-v1.json")
    fun report(name: String, flags: Map<String, Boolean> = emptyMap(), counts: Map<String, Int> = emptyMap()) {
        phase = name
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefSecurityPhase", name)
            flags.forEach { (key, value) -> putBoolean("passchefSecurity_$key", value) }
            counts.forEach { (key, value) -> putInt("passchefSecurity_$key", value) }
        })
    }
    fun foreground() {
        val flags = timerMain {
            CookbookUiDiagnostics.environmentFlags(activity) + CookbookUiDiagnostics.stateFlags(activity) + mapOf(
                "resumed" to (activity.lifecycle.currentState == Lifecycle.State.RESUMED),
                "secureFlag" to (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
                "recordAbsent" to !file.exists())
        }
        report(phase, flags)
        assertTrue("Real secure locked own foreground required", flags["resumed"] == true &&
            flags["powerInteractive"] == true && flags["keyguardLocked"] == false && flags["deviceLocked"] == false &&
            flags["proofWindowFocused"] == true && flags["proofWindowAttached"] == true && flags["proofWindowShown"] == true &&
            flags["secureFlag"] == true && flags["recordAbsent"] == true && flags["vaultUnlocked"] == false &&
            flags["vaultReady"] == false && flags["hasDish"] == false && flags["dishNonempty"] == false)
    }
    fun target(node: SemanticsNode) {
        val flags = timerMain {
            val bounds = node.boundsInWindow
            val decor = activity.window.decorView
            val offset = IntArray(2).also(decor::getLocationOnScreen)
            val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
            val density = activity.resources.displayMetrics.density
            val left = bounds.left + offset[0]; val top = bounds.top + offset[1]
            val right = bounds.right + offset[0]; val bottom = bounds.bottom + offset[1]
            mapOf("targetFinite" to listOf(left, top, right, bottom, density).all { it.isFinite() },
                "target48dp" to (density > 0 && bounds.width >= 48 * density && bounds.height >= 48 * density),
                "targetFullyVisible" to (left >= visible.left && top >= visible.top && right <= visible.right && bottom <= visible.bottom),
                "targetInsideOwnWindow" to (left >= offset[0] && top >= offset[1] &&
                    right <= offset[0] + decor.width && bottom <= offset[1] + decor.height))
        }
        report(phase, flags)
        measure(node)
        assertTrue("Selected actual visible controls require finite own-window bounds and 48dp targets", flags.values.all { it })
    }
    /** Visual and minimum-touch bounds differ; diagnostics do not replace the original assertions. */
    fun measure(node: SemanticsNode) {
        val measured = timerMain {
            val visual = node.boundsInWindow; val root = node.boundsInRoot; val touch = node.touchBoundsInRoot
            val decor = activity.window.decorView
            val offset = IntArray(2).also(decor::getLocationOnScreen)
            val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
            val density = activity.resources.displayMetrics.density
            val rootScreenX = offset[0] + visual.left - root.left
            val rootScreenY = offset[1] + visual.top - root.top
            fun inside(left: Float, top: Float, right: Float, bottom: Float) =
                left >= visible.left && top >= visible.top && right <= visible.right && bottom <= visible.bottom
            mapOf("geometryClickAction" to node.config.contains(SemanticsActions.OnClick),
                "geometryTouch48dp" to (density > 0 && touch.width >= 48 * density && touch.height >= 48 * density),
                "geometryVisualFullyVisible" to inside(visual.left + offset[0], visual.top + offset[1], visual.right + offset[0], visual.bottom + offset[1]),
                "geometryTouchFullyVisible" to inside(touch.left + rootScreenX, touch.top + rootScreenY, touch.right + rootScreenX, touch.bottom + rootScreenY)) to
                mapOf("visualWidthPx" to visual.width.toInt(), "visualHeightPx" to visual.height.toInt(),
                    "touchWidthPx" to touch.width.toInt(), "touchHeightPx" to touch.height.toInt(),
                    "densityX1000" to (density * 1000).toInt(),
                    "displayWidthPx" to activity.resources.displayMetrics.widthPixels,
                    "displayHeightPx" to activity.resources.displayMetrics.heightPixels,
                    "visibleWidthPx" to visible.width(), "visibleHeightPx" to visible.height(),
                    "rootScreenOffsetXPx" to rootScreenX.toInt(), "rootScreenOffsetYPx" to rootScreenY.toInt())
        }
        report(phase, measured.first, measured.second)
    }
    fun fieldAccessibility(node: SemanticsNode, label: String) {
        foreground()
        val measured = securityPasswordAccessibility(automation, activity.packageName, node.id, label)
        report(phase, measured.first, measured.second)
        foreground()
    }
    fun accessibility(label: String) {
        foreground()
        val root = automation.rootInActiveWindow
        assertTrue("Actual accessibility window is available", root != null)
        var visited = 0; var passwords = 0; var labelFound = false; var localizedPasswords = 0
        fun visit(node: AccessibilityNodeInfo) {
            assertTrue("Actual accessibility observation is bounded", ++visited <= 500)
            if (node.packageName?.toString() == activity.packageName) {
                if (node.isPassword) passwords++
                val localized = node.text?.contains(label) == true || node.hintText?.contains(label) == true
                if (localized) labelFound = true
                if (localized && node.isVisibleToUser && node.isPassword && node.isEditable) localizedPasswords++
            }
            for (index in 0 until node.childCount) node.getChild(index)?.let { child ->
                try { visit(child) } finally { child.recycle() }
            }
        }
        try {
            assertTrue("Actual accessibility root belongs to the proof app", root!!.packageName?.toString() == activity.packageName)
            visit(root)
        } finally { root?.recycle() }
        report(phase, mapOf("accessiblePasswordPresent" to (passwords > 0), "accessibleLocalizedLabel" to labelFound,
            "accessibleLocalizedPasswordControl" to (localizedPasswords > 0)),
            mapOf("accessibilityNodes" to visited, "passwordNodes" to passwords, "localizedPasswordNodes" to localizedPasswords))
        assertTrue("Actual localized editable password control is visible in app accessibility", localizedPasswords > 0)
        foreground()
    }
    fun capture() {
        foreground()
        val rectangle = timerMain {
            Rect().also { rect ->
                val content = activity.findViewById<android.view.View>(android.R.id.content)
                assertTrue("Actual own content is visible", content.getLocalVisibleRect(rect))
                val offset = IntArray(2).also(content::getLocationOnScreen)
                rect.offset(offset[0], offset[1])
                val margin = (4 * activity.resources.displayMetrics.density).toInt().coerceAtLeast(1)
                rect.inset(margin, margin)
            }
        }
        val bitmap = securityDisplayCapture("passchefSecurity")
        assertTrue("Real display screenshot must be available; null is not secure capture proof", bitmap != null)
        var black = 0; var pixels = 0
        try {
            val image = requireNotNull(bitmap)
            assertTrue("Capture covers a valid actual own content rectangle", rectangle.width() > 0 && rectangle.height() > 0 &&
                rectangle.left >= 0 && rectangle.top >= 0 && rectangle.right <= image.width && rectangle.bottom <= image.height)
            val row = IntArray(rectangle.width())
            for (y in rectangle.top until rectangle.bottom) {
                image.getPixels(row, 0, row.size, rectangle.left, y, row.size, 1)
                for (pixel in row) {
                    pixels++
                    if (Color.alpha(pixel) == 255 && Color.red(pixel) == 0 && Color.green(pixel) == 0 && Color.blue(pixel) == 0) black++
                }
            }
            report(phase, mapOf("captureAvailable" to true, "ownContentAllBlack" to (black == pixels)),
                mapOf("captureWidth" to image.width, "captureHeight" to image.height, "ownWidth" to rectangle.width(),
                    "ownHeight" to rectangle.height(), "ownPixels" to pixels, "blackPixels" to black))
            assertTrue("Real secure app content is fully excluded from display capture", pixels > 0 && black == pixels)
        } finally { bitmap?.recycle() }
        foreground()
    }
    fun failure(error: Throwable) {
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefSecurityPhase", "$phase-failed")
            putString("passchefSecurityFailureClass", error.javaClass.simpleName)
        })
    }
}

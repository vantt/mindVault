package dev.passchef.mobile.proof

import android.graphics.Rect
import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/** Actual own Compose text/layout and controller bits share a main-thread observation; no text/pixels leave RAM. */
fun manualVisualSnapshot(ui: ComposeTestRule, activity: CookbookProofActivity,
    stateOnMain: () -> Map<String, Boolean>): Map<String, Boolean> {
    val nodes = runCatching { ui.onAllNodesWithTag("dish-display").fetchSemanticsNodes() }
    return timerMain {
        val state = stateOnMain()
        val flags = mutableMapOf("visualSemanticsReadSucceeded" to nodes.isSuccess,
            "visualDishPresent" to (nodes.getOrNull()?.isNotEmpty() == true),
            "visualDishUnique" to (nodes.getOrNull()?.size == 1), "visualTextReadSucceeded" to false,
            "visualTextMatchesMask" to false, "visualTextMatchesSynthetic" to false,
            "visualLayoutReadSucceeded" to false, "visualLayoutAttached" to false, "visualLayoutPlaced" to false,
            "visualBoundsFinite" to false, "visualBoundsNonempty" to false,
            "visualOwnContentVisible" to false, "visualFullInsideOwnContent" to false,
            "visualFullInsideWindowFrame" to false, "visualImeHidden" to false,
            "visualOwnForegroundSecure" to (state["secureFlag"] == true && state["resumed"] == true &&
                state["proofWindowFocused"] == true && state["proofWindowAttached"] == true && state["proofWindowShown"] == true &&
                state["powerInteractive"] == true && state["keyguardLocked"] == false && state["deviceLocked"] == false))
        nodes.getOrNull()?.singleOrNull()?.let { node ->
            runCatching {
                val config = node.config
                if (config.contains(SemanticsProperties.Text)) {
                    val text = config[SemanticsProperties.Text].joinToString("") { it.text }
                    flags["visualTextReadSucceeded"] = true
                    flags["visualTextMatchesMask"] = text == "••••••••"
                    flags["visualTextMatchesSynthetic"] = text == "aB!*abcd"
                }
            }
            runCatching {
                val layout = node.layoutInfo
                val coordinates = layout.coordinates
                flags["visualLayoutAttached"] = layout.isAttached && coordinates.isAttached
                flags["visualLayoutPlaced"] = layout.isPlaced
                val corners = listOf(Offset.Zero, Offset(coordinates.size.width.toFloat(), 0f),
                    Offset(0f, coordinates.size.height.toFloat()),
                    Offset(coordinates.size.width.toFloat(), coordinates.size.height.toFloat())).map(coordinates::localToWindow)
                val decor = activity.window.decorView
                val windowOffset = IntArray(2).also(decor::getLocationOnScreen)
                val points = corners.map { Offset(it.x + windowOffset[0], it.y + windowOffset[1]) }
                val finite = points.all { it.x.isFinite() && it.y.isFinite() }
                val nonempty = coordinates.size.width > 0 && coordinates.size.height > 0
                flags["visualBoundsFinite"] = finite; flags["visualBoundsNonempty"] = nonempty
                val content = activity.findViewById<View>(android.R.id.content)
                val ownVisible = Rect()
                val ownVisibleRead = content.getLocalVisibleRect(ownVisible)
                val contentOffset = IntArray(2).also(content::getLocationOnScreen)
                ownVisible.offset(contentOffset[0], contentOffset[1])
                val windowVisible = Rect().also(decor::getWindowVisibleDisplayFrame)
                fun contains(rect: Rect) = finite && nonempty && points.all {
                    it.x >= rect.left && it.y >= rect.top && it.x <= rect.right && it.y <= rect.bottom
                }
                flags["visualOwnContentVisible"] = ownVisibleRead
                flags["visualFullInsideOwnContent"] = ownVisibleRead && contains(ownVisible)
                flags["visualFullInsideWindowFrame"] = contains(windowVisible)
                flags["visualImeHidden"] = ViewCompat.getRootWindowInsets(decor)?.isVisible(WindowInsetsCompat.Type.ime()) == false
                flags["visualLayoutReadSucceeded"] = true
            }
        }
        state + flags
    }
}

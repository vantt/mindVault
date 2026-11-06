package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.os.Bundle
import android.os.SystemClock
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue

/** Native accessibility actions on the existing restored task; never launches/recreates an Activity. */
class SavedTaskUiActions(private val automation: UiAutomation, private val packageName: String,
    private val foreground: () -> Boolean, private val staticLabelsOnly: Boolean = false) {
    private fun named(node: AccessibilityNodeInfo, label: String): Boolean {
        val text = if (staticLabelsOnly && (node.isEditable || node.isPassword)) null else node.text
        return listOf(text, node.hintText, node.contentDescription).any { it?.toString() == label }
    }
    private fun labels(node: AccessibilityNodeInfo, label: String, budget: IntArray): Boolean {
        assertTrue("Own label observation is bounded", ++budget[0] <= 500)
        if (node.packageName?.toString() != packageName) return false
        if (named(node, label)) return true
        for (index in 0 until node.childCount) node.getChild(index)?.let { child ->
            try { if (labels(child, label, budget)) return true } finally { child.recycle() }
        }
        return false
    }
    private fun matchesLabel(node: AccessibilityNodeInfo, label: String, editable: Boolean?): Boolean = when (editable) {
        null -> named(node, label)
        true -> node.isEditable && labels(node, label, intArrayOf(0))
        false -> node.isClickable && labels(node, label, intArrayOf(0))
    }
    private fun find(label: String, editable: Boolean?): AccessibilityNodeInfo? {
        assertTrue("Native actions require the actual own focused restored Activity", foreground())
        automation.clearCache()
        val root = automation.rootInActiveWindow ?: return null
        val matches = mutableListOf<AccessibilityNodeInfo>()
        var count = 0
        fun visit(node: AccessibilityNodeInfo) {
            assertTrue("Own accessibility tree observation is bounded", ++count <= 500)
            if (node.packageName?.toString() != packageName) return
            if (node.isEnabled && matchesLabel(node, label, editable)) {
                matches += AccessibilityNodeInfo.obtain(node)
            }
            for (index in 0 until node.childCount) node.getChild(index)?.let { child -> try { visit(child) } finally { child.recycle() } }
        }
        try {
            assertTrue("Actual accessibility root is the restored app", root.packageName?.toString() == packageName)
            visit(root)
            assertTrue("Known own control is unambiguous", matches.size <= 1)
            return matches.singleOrNull()
        } catch (failure: Throwable) { matches.forEach { it.recycle() }; throw failure }
        finally { root.recycle() }
    }
    private fun act(label: String, editable: Boolean?, action: (AccessibilityNodeInfo) -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 15_000
        var forward = true
        var forwardCount = 0
        var backwardCount = 0
        var boundaries = 0
        var scrollAttempts = 0
        var showAttempted = false
        fun report() {
            if (scrollAttempts == 0) return
            InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
                putInt("passchefSavedTaskScrollAttempts", scrollAttempts)
                putInt("passchefSavedTaskScrollForward", forwardCount)
                putInt("passchefSavedTaskScrollBackward", backwardCount)
                putInt("passchefSavedTaskScrollBoundaries", boundaries)
            })
        }
        do {
            val node = find(label, editable)
            if (node != null) {
                try {
                    if (node.refresh() && node.isVisibleToUser) {
                        assertTrue("Known native control stays in the actual own foreground", foreground() &&
                            node.packageName?.toString() == packageName && node.isEnabled &&
                            matchesLabel(node, label, editable))
                        assertTrue("Actual native action succeeds on the known own control", action(node))
                        report()
                        return
                    }
                    if (!showAttempted) {
                        showAttempted = true
                        if (node.performAction(AccessibilityAction.ACTION_SHOW_ON_SCREEN.id)) {
                            Thread.sleep(100)
                            continue
                        }
                    }
                } finally { node.recycle() }
            }
            if (scrollAttempts >= 64) break
            scrollAttempts++
            if (scroll(forward)) {
                if (forward) forwardCount++ else backwardCount++
            } else {
                boundaries++
                forward = !forward
            }
            Thread.sleep(100)
        } while (SystemClock.elapsedRealtime() < deadline)
        report()
        throw AssertionError("Known own native control did not become visible")
    }
    private fun scroll(forward: Boolean): Boolean {
        assertTrue("Native scrolling requires the actual own focused Activity", foreground())
        automation.clearCache()
        val root = automation.rootInActiveWindow ?: return false
        var outer: AccessibilityNodeInfo? = null
        var visited = 0
        fun visit(node: AccessibilityNodeInfo) {
            assertTrue("Own native scroll observation is bounded", ++visited <= 500)
            if (node.packageName?.toString() != packageName) return
            if (outer == null && node.isScrollable && !node.isEditable && node.isEnabled && node.isVisibleToUser) {
                outer = AccessibilityNodeInfo.obtain(node)
            }
            for (index in 0 until node.childCount) node.getChild(index)?.let { child ->
                try { visit(child) } finally { child.recycle() }
            }
        }
        try {
            assertTrue("Native scroll root stays in the own app", root.packageName?.toString() == packageName)
            visit(root)
            val target = outer ?: return false
            val action = if (forward) AccessibilityNodeInfo.ACTION_SCROLL_FORWARD else AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
            assertTrue("Only the actual own noneditable visible scroller accepts movement", foreground() &&
                target.refresh() && target.packageName?.toString() == packageName && target.isScrollable &&
                target.isEnabled && target.isVisibleToUser && !target.isEditable)
            // Choose the outermost real scroller first. Its boundary changes search direction,
            // rather than scrolling an inner Recipe text editor or fabricating input coordinates.
            return target.actionList.any { it.id == action } && target.performAction(action)
        } finally { outer?.recycle(); root.recycle() }
    }
    fun fill(label: String, value: String) = act(label, true) { node ->
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value)
        })
    }
    fun show(label: String) = act(label, false) { true }
    fun showText(label: String) = act(label, null) { true }
    fun showEditable(label: String) = act(label, true) { true }
    fun click(label: String) = act(label, false) { it.performAction(AccessibilityNodeInfo.ACTION_CLICK) }
    fun hasText(label: String): Boolean {
        assertTrue("Text observation requires the own foreground", foreground())
        automation.clearCache()
        val root = automation.rootInActiveWindow ?: return false
        try {
            assertTrue("Text observation stays in the own app", root.packageName?.toString() == packageName)
            return labels(root, label, intArrayOf(0))
        } finally { root.recycle() }
    }
}

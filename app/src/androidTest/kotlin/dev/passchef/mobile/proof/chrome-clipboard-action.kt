package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.Process
import android.os.SystemClock
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue

/** Only a focused empty non-password Chrome editor may receive this public synthetic marker. */
class ChromeClipboardAction(private val marker: String) {
    private val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
    private val packageName = "com.android.chrome"
    val uid: Int
    init {
        val descriptor = automation.executeShellCommand("cmd package list packages -U com.android.chrome")
        val bytes = ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { input ->
            val output = java.io.ByteArrayOutputStream()
            val buffer = ByteArray(512)
            while (true) {
                val read = input.read(buffer)
                if (read < 0) break
                require(output.size() + read <= 2048) { "Bounded package identity required" }
                output.write(buffer, 0, read)
            }
            output.toByteArray()
        }
        val identities = String(bytes, Charsets.UTF_8).lineSequence().map(String::trim)
            .mapNotNull { Regex("package:com\\.android\\.chrome uid:(\\d+)").matchEntire(it)?.groupValues?.get(1)?.toInt() }.toList()
        assertTrue("Exactly one installed external Chrome UID required", identities.size == 1)
        uid = identities.single()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        assertTrue("Chrome differs from both target and instrumentation app UIDs", uid > 0 && uid != Process.myUid() &&
            uid != instrumentation.targetContext.applicationInfo.uid && uid != instrumentation.context.applicationInfo.uid)
    }
    private fun awaitField(field: AccessibilityNodeInfo, predicate: () -> Boolean): Boolean {
        val deadline = SystemClock.elapsedRealtime() + 5_000
        do {
            if (field.refresh() && predicate()) return true
            Thread.sleep(25)
        } while (SystemClock.elapsedRealtime() < deadline)
        return false
    }
    private fun currentField(field: AccessibilityNodeInfo, windowId: Int, selectionRequired: Boolean): Boolean {
        automation.clearCache()
        val active = automation.rootInActiveWindow ?: return false
        try {
            return active.packageName?.toString() == packageName && active.windowId == windowId && field.refresh() &&
                field.windowId == windowId && field.packageName?.toString() == packageName && field.isFocused &&
                field.isEditable && field.isVisibleToUser && field.isEnabled && !field.isPassword && field.text?.toString() == marker &&
                (!selectionRequired || (field.textSelectionStart == 0 && field.textSelectionEnd == marker.length))
        } finally { active.recycle() }
    }
    fun copyFromEmptyEditor(beforeCopy: () -> Unit): Boolean {
        automation.clearCache()
        val root = automation.rootInActiveWindow ?: return false
        try {
            if (root.packageName?.toString() != packageName) return false
            val field = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT) ?: return false
            try {
                if (field.packageName?.toString() != packageName || !field.isEditable || !field.isEnabled || !field.isVisibleToUser ||
                    !field.isFocused || field.isPassword || !field.text.isNullOrEmpty() || field.windowId != root.windowId) return false
                if (listOf(AccessibilityNodeInfo.ACTION_SET_TEXT, AccessibilityNodeInfo.ACTION_SET_SELECTION)
                        .any { action -> field.actionList.none { it.id == action } }) return false
                assertTrue("Actual empty Chrome field accepts only the public marker", field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,
                    Bundle().apply { putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, marker) }))
                assertTrue("Chrome text remains exact before selecting", awaitField(field) { field.packageName?.toString() == packageName &&
                    field.isFocused && field.text?.toString() == marker && !field.isPassword })
                assertTrue("Chrome selects the entire public marker", field.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION,
                    Bundle().apply {
                        putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0)
                        putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, marker.length)
                    }))
                assertTrue("Actual Chrome selection and Copy action exist", awaitField(field) { field.text?.toString() == marker &&
                    field.textSelectionStart == 0 && field.textSelectionEnd == marker.length &&
                    field.actionList.any { it.id == AccessibilityNodeInfo.ACTION_COPY } })
                beforeCopy()
                assertTrue("Chrome window/selection are current immediately before Copy", currentField(field, root.windowId, true))
                assertTrue("Chrome performs its real selection Copy action", field.performAction(AccessibilityNodeInfo.ACTION_COPY))
                assertTrue("Chrome window and marker remain current immediately after Copy", currentField(field, root.windowId, false))
                return true
            } finally { field.recycle() }
        } finally { root.recycle() }
    }
}

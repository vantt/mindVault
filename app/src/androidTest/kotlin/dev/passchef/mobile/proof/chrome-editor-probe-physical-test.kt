package dev.passchef.mobile.proof

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import android.view.accessibility.AccessibilityNodeInfo
import org.junit.Test
import org.junit.runner.RunWith

/** Read-only editor capabilities; no Activity launch, text mutation or clipboard access. */
@RunWith(AndroidJUnit4::class)
class ChromeEditorProbePhysicalTest {
    @Test fun focusedChromeEditorCapabilities() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val automation = instrumentation.uiAutomation
        automation.clearCache()
        val root = automation.rootInActiveWindow
        val field = root?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        try {
            val actions = field?.actionList.orEmpty().map { it.id }
            val flags = linkedMapOf(
                "rootPresent" to (root != null),
                "rootChrome" to (root?.packageName?.toString() == "com.android.chrome"),
                "inputFocusPresent" to (field != null),
                "fieldChrome" to (field?.packageName?.toString() == "com.android.chrome"),
                "sameWindow" to (field != null && field.windowId == root?.windowId),
                "editable" to (field?.isEditable == true),
                "enabled" to (field?.isEnabled == true),
                "visible" to (field?.isVisibleToUser == true),
                "focused" to (field?.isFocused == true),
                "password" to (field?.isPassword == true),
                "textNull" to (field != null && field.text == null),
                "textEmpty" to (field != null && field.text.isNullOrEmpty()),
                "showingHint" to (field?.isShowingHintText == true),
                "hasSetText" to (AccessibilityNodeInfo.ACTION_SET_TEXT in actions),
                "hasSetSelection" to (AccessibilityNodeInfo.ACTION_SET_SELECTION in actions),
                "hasCopy" to (AccessibilityNodeInfo.ACTION_COPY in actions)
            )
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefEditorPhase", "read-only-capabilities-sampled")
                flags.forEach { (key, value) -> putBoolean("passchefEditor_$key", value) }
            })
        } finally {
            try { field?.recycle() } finally { root?.recycle() }
        }
    }
}

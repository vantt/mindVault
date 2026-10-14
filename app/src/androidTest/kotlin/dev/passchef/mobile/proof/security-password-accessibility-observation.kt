package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo

/** Field identity diagnostics only: no text/tree output and no accessibility success assertion. */
fun securityPasswordAccessibility(automation: UiAutomation, packageName: String, fieldId: Int, label: String):
    Pair<Map<String, Boolean>, Map<String, Int>> {
    val flags = mutableMapOf("fieldWindowAvailable" to false, "fieldOwnWindow" to false,
        "fieldTraversalComplete" to true, "fieldSemanticsExtraPresent" to false, "fieldTargetFound" to false,
        "fieldTargetVisible" to false, "fieldTargetEditable" to false, "fieldTargetPassword" to false,
        "fieldLocalizedText" to false, "fieldLocalizedHint" to false, "fieldLocalizedDescription" to false,
        "fieldLocalizedDescendants" to false, "fieldIdExtraRequestSupported" to false,
        "fieldIdExtraRequestSucceeded" to false)
    var visited = 0; var matched = 0; var descendants = 0
    val root = automation.rootInActiveWindow
    flags["fieldWindowAvailable"] = root != null
    flags["fieldOwnWindow"] = root?.packageName?.toString() == packageName
    fun contains(value: CharSequence?) = value?.contains(label) == true
    fun descendantLabel(node: AccessibilityNodeInfo): Boolean {
        if (descendants >= 100) { flags["fieldTraversalComplete"] = false; return false }
        descendants++
        var found = node.packageName?.toString() == packageName &&
            (contains(node.text) || contains(node.hintText) || contains(node.contentDescription))
        for (index in 0 until node.childCount) {
            if (descendants >= 100) { flags["fieldTraversalComplete"] = false; break }
            node.getChild(index)?.let { child ->
                try { if (descendantLabel(child)) found = true } finally { child.recycle() }
            }
        }
        return found
    }
    fun visit(node: AccessibilityNodeInfo) {
        if (visited >= 500) { flags["fieldTraversalComplete"] = false; return }
        visited++
        if (node.packageName?.toString() == packageName) {
            val key = "androidx.compose.ui.semantics.id"
            if (node.availableExtraData.contains(key)) {
                flags["fieldIdExtraRequestSupported"] = true
                if (node.refreshWithExtraData(key, Bundle())) flags["fieldIdExtraRequestSucceeded"] = true
            }
            val hasId = node.extras.containsKey(key)
            if (hasId) flags["fieldSemanticsExtraPresent"] = true
            if (hasId && node.extras.getInt(key, -1) == fieldId) {
                matched++
                flags["fieldTargetFound"] = true
                flags["fieldTargetVisible"] = flags["fieldTargetVisible"] == true || node.isVisibleToUser
                flags["fieldTargetEditable"] = flags["fieldTargetEditable"] == true || node.isEditable
                flags["fieldTargetPassword"] = flags["fieldTargetPassword"] == true || node.isPassword
                flags["fieldLocalizedText"] = flags["fieldLocalizedText"] == true || contains(node.text)
                flags["fieldLocalizedHint"] = flags["fieldLocalizedHint"] == true || contains(node.hintText)
                flags["fieldLocalizedDescription"] = flags["fieldLocalizedDescription"] == true || contains(node.contentDescription)
                for (index in 0 until node.childCount) {
                    if (descendants >= 100) { flags["fieldTraversalComplete"] = false; break }
                    node.getChild(index)?.let { child ->
                        try { if (descendantLabel(child)) flags["fieldLocalizedDescendants"] = true } finally { child.recycle() }
                    }
                }
            }
        }
        for (index in 0 until node.childCount) {
            if (visited >= 500) { flags["fieldTraversalComplete"] = false; break }
            node.getChild(index)?.let { child ->
                try { visit(child) } finally { child.recycle() }
            }
        }
    }
    try { if (flags["fieldOwnWindow"] == true && root != null) visit(root) } finally { root?.recycle() }
    flags["fieldSingleMatch"] = matched == 1
    return flags to mapOf("fieldRequestedSemanticsId" to fieldId, "fieldVisitedNodes" to visited,
        "fieldMatchedNodes" to matched, "fieldDescendantNodes" to descendants)
}

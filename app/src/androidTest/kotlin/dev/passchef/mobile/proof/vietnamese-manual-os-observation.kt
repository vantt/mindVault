package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo

data class VietnameseManualOsSnapshot(val flags: Map<String, Boolean>, val counts: Map<String, Int>)

/** Fresh own OS label metadata only; editable/password node text is never read. */
fun readVietnameseManualOs(automation: UiAutomation, packageName: String): VietnameseManualOsSnapshot {
    val flags = mutableMapOf("ownWindow" to false, "complete" to true, "fieldVisible" to false,
        "fieldEditable" to false, "fieldPassword" to false, "fieldLabelVi" to false, "fieldLabelEn" to false,
        "fieldSemanticsIdPresent" to false, "a11yFocusWithinField" to false, "inputFocusWithinField" to false)
    var visited = 0; var descendants = 0; var matched = 0; var english = 0; var passwords = 0
    var extraRequests = 0; var targetId: Int? = null
    flags["cacheCleared"] = automation.clearCache()
    val root = automation.rootInActiveWindow
    var a11y: AccessibilityNodeInfo? = null
    var input: AccessibilityNodeInfo? = null
    data class Labels(val vi: Boolean, val en: Boolean, val a11yWithin: Boolean, val inputWithin: Boolean)
    fun labels(node: AccessibilityNodeInfo): Pair<Boolean, Boolean> {
        if (node.packageName?.toString() != packageName) return false to false
        val values = mutableListOf(node.hintText, node.contentDescription)
        if (!node.isEditable && !node.isPassword) values += node.text
        return values.any { it?.toString() == "Mật khẩu chính" } to values.any { it?.toString() == "Master password" }
    }
    fun semanticsId(node: AccessibilityNodeInfo): Int? {
        val key = "androidx.compose.ui.semantics.id"
        if (node.packageName?.toString() != packageName || !node.availableExtraData.contains(key)) return null
        extraRequests++
        if (!node.refreshWithExtraData(key, Bundle()) || node.packageName?.toString() != packageName) return null
        return if (node.extras.containsKey(key)) node.extras.getInt(key, -1).takeIf { it >= 0 } else null
    }
    fun descend(node: AccessibilityNodeInfo): Labels {
        if (descendants >= 100) { flags["complete"] = false; return Labels(false, false, false, false) }
        descendants++
        if (node.packageName?.toString() != packageName) return Labels(false, false, false, false)
        var (vi, en) = labels(node)
        var a11yWithin = node == a11y; var inputWithin = node == input
        for (i in 0 until node.childCount) {
            if (descendants >= 100) { flags["complete"] = false; break }
            node.getChild(i)?.let { child ->
                try {
                    val next = descend(child)
                    vi = vi || next.vi; en = en || next.en
                    a11yWithin = a11yWithin || next.a11yWithin; inputWithin = inputWithin || next.inputWithin
                } finally { child.recycle() }
            }
        }
        return Labels(vi, en, a11yWithin, inputWithin)
    }
    fun visit(node: AccessibilityNodeInfo) {
        if (visited >= 500) { flags["complete"] = false; return }
        visited++
        if (node.packageName?.toString() != packageName) return
        if (node.isEditable && node.isPassword) {
            if (!node.refresh() || node.packageName?.toString() != packageName) flags["complete"] = false
            else if (node.isEditable && node.isPassword) {
                passwords++
                val id = semanticsId(node)
                val label = descend(node)
                if (label.en) english++
                if (label.vi) {
                    matched++; targetId = id
                    flags["fieldVisible"] = node.isVisibleToUser
                    flags["fieldEditable"] = node.isEditable; flags["fieldPassword"] = node.isPassword
                    flags["fieldLabelVi"] = label.vi; flags["fieldLabelEn"] = label.en
                    flags["fieldSemanticsIdPresent"] = id != null
                    flags["a11yFocusWithinField"] = label.a11yWithin
                    flags["inputFocusWithinField"] = label.inputWithin
                }
            }
        }
        for (i in 0 until node.childCount) {
            if (visited >= 500) { flags["complete"] = false; break }
            node.getChild(i)?.let { child -> try { visit(child) } finally { child.recycle() } }
        }
    }
    fun focus(node: AccessibilityNodeInfo?, name: String) {
        flags["${name}Present"] = node != null
        flags["${name}Own"] = node?.packageName?.toString() == packageName
        if (node == null || flags["${name}Own"] != true) return
        val before = labels(node)
        flags["${name}LabelViBeforeRefresh"] = before.first; flags["${name}LabelEnBeforeRefresh"] = before.second
        flags["${name}RefreshSucceeded"] = node.refresh()
        val after = labels(node)
        flags["${name}LabelViAfterRefresh"] = after.first; flags["${name}LabelEnAfterRefresh"] = after.second
        flags["${name}MatchesFieldId"] = matched == 1 && targetId != null && semanticsId(node) == targetId
    }
    try {
        flags["ownWindow"] = root?.packageName?.toString() == packageName
        if (root != null && flags["ownWindow"] == true) {
            a11y = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)
            input = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
            visit(root) // Determine the unique VI Master field before comparing either focus ID.
            focus(a11y, "a11yFocus"); focus(input, "inputFocus")
        }
    } finally { a11y?.recycle(); input?.recycle(); root?.recycle() }
    flags["singleField"] = matched == 1
    if (matched != 1) { flags["a11yFocusWithinField"] = false; flags["inputFocusWithinField"] = false }
    return VietnameseManualOsSnapshot(flags, mapOf("fieldSemanticsId" to (targetId ?: -1), "visitedNodes" to visited,
        "labelDescendants" to descendants, "matchedFields" to matched, "englishPasswordFields" to english,
        "passwordFields" to passwords, "extraRequests" to extraRequests))
}

package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.os.Bundle
import android.os.SystemClock
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** New API33+ VI-only observer; no authentication, screenshot or TalkBack acceptance claim. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 33)
class VietnamesePasswordAccessibilityPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private var phase = "vi-password-start"
    private fun report(name: String, flags: Map<String, Boolean>, counts: Map<String, Int>) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefVietnameseAccessibilityPhase", name)
            flags.forEach { (key, value) -> putBoolean("passchefVietnameseAccessibility_$key", value) }
            counts.forEach { (key, value) -> putInt("passchefVietnameseAccessibility_$key", value) }
        })
    }
    private fun observe(automation: UiAutomation, packageName: String, fieldId: Int, label: String):
        Pair<Map<String, Boolean>, Map<String, Int>> {
        val flags = mutableMapOf("ownWindow" to false, "complete" to true, "targetVisible" to false,
            "targetEditable" to false, "targetPassword" to false, "labelSelf" to false, "labelDescendants" to false,
            "extraRequested" to false, "extraSucceeded" to false)
        var visited = 0; var descendants = 0; var matched = 0; var requested = 0; var refreshed = 0
        fun named(node: AccessibilityNodeInfo) = listOf(node.text, node.hintText, node.contentDescription).any { it?.contains(label) == true }
        fun descendantLabel(source: AccessibilityNodeInfo): Boolean {
            if (descendants >= 100) { flags["complete"] = false; return false }
            val node = AccessibilityNodeInfo.obtain(source)
            try {
                descendants++
                if (node.packageName?.toString() != packageName) return false
                var found = named(node)
                for (index in 0 until node.childCount) {
                    if (descendants >= 100) { flags["complete"] = false; break }
                    node.getChild(index)?.let { child ->
                        try { if (descendantLabel(child)) found = true } finally { child.recycle() }
                    }
                }
                return found
            } finally { node.recycle() }
        }
        fun visit(source: AccessibilityNodeInfo) {
            if (visited >= 500) { flags["complete"] = false; return }
            val node = AccessibilityNodeInfo.obtain(source)
            try {
                visited++
                if (node.packageName?.toString() != packageName) return
                val key = "androidx.compose.ui.semantics.id"
                if (node.availableExtraData.contains(key)) {
                    requested++; flags["extraRequested"] = true
                    if (node.refreshWithExtraData(key, Bundle())) { refreshed++; flags["extraSucceeded"] = true }
                }
                if (node.extras.containsKey(key) && node.extras.getInt(key, -1) == fieldId) {
                    matched++
                    flags["targetVisible"] = flags["targetVisible"] == true || node.isVisibleToUser
                    flags["targetEditable"] = flags["targetEditable"] == true || node.isEditable
                    flags["targetPassword"] = flags["targetPassword"] == true || node.isPassword
                    flags["labelSelf"] = flags["labelSelf"] == true || named(node)
                    for (index in 0 until node.childCount) {
                        if (descendants >= 100) { flags["complete"] = false; break }
                        node.getChild(index)?.let { child ->
                            try { if (descendantLabel(child)) flags["labelDescendants"] = true } finally { child.recycle() }
                        }
                    }
                }
                for (index in 0 until node.childCount) {
                    if (visited >= 500) { flags["complete"] = false; break }
                    node.getChild(index)?.let { child -> try { visit(child) } finally { child.recycle() } }
                }
            } finally { node.recycle() }
        }
        val root = automation.rootInActiveWindow
        try {
            flags["ownWindow"] = root?.packageName?.toString() == packageName
            if (flags["ownWindow"] == true && root != null) visit(root)
        } finally { root?.recycle() }
        flags["singleMatch"] = matched == 1
        return flags to mapOf("requestedFieldId" to fieldId, "matchedNodes" to matched, "visitedNodes" to visited,
            "descendantNodes" to descendants, "extraRequests" to requested, "extraSuccesses" to refreshed)
    }
    @Test fun realLockedVietnamesePasswordFieldHasOwnedOsLabelAssociation() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val guards = SecurityUiObservation(activity)
        val automation = InstrumentationRegistry.getInstrumentation().getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
        try {
            ui.waitForIdle(); guards.foreground()
            if (ui.onAllNodesWithText("VI").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("VI").performClick()
            ui.waitForIdle()
            ui.onNodeWithText("PassChef — Cookbook riêng").assertExists()
            val label = "Mật khẩu chính"
            ui.onNode(hasSetTextAction() and hasText(label)).performScrollTo()
            ui.waitForIdle()
            val deadline = SystemClock.elapsedRealtime() + 10_000
            var accepted = false; var attempts = 0
            while (!accepted && SystemClock.elapsedRealtime() < deadline) {
                guards.foreground()
                val reset = automation.clearCache()
                val field = ui.onNode(hasSetTextAction() and hasText(label))
                field.assertIsDisplayed().assertIsEnabled().assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
                val measured = observe(automation, activity.packageName, field.fetchSemanticsNode().id, label)
                attempts++
                report("vi-password-observation", measured.first + ("observerCacheCleared" to reset), measured.second + ("attempts" to attempts))
                val flags = measured.first
                accepted = reset && listOf("ownWindow", "complete", "singleMatch", "targetVisible", "targetEditable", "targetPassword",
                    "extraRequested", "extraSucceeded").all { flags[it] == true } &&
                    (flags["labelSelf"] == true || flags["labelDescendants"] == true)
                guards.foreground()
                if (!accepted) Thread.sleep(200)
            }
            assertTrue("Fresh VI OS field identity/visible editable password/localized association required", accepted)
            assertFalse("Locked VI observer creates no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
            report("vi-password-association-passed", mapOf("passed" to true), mapOf("attempts" to attempts))
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", mapOf("failed" to true), emptyMap()) }
            throw AssertionError("Vietnamese password association failed; safe metadata recorded")
        } finally { ui.activityRule.scenario.close(); directory.deleteRecursively() }
    }
}

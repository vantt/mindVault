package dev.passchef.mobile.proof

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Selected physical locked-screen checks only; no setup, authentication or policy changes. */
@RunWith(AndroidJUnit4::class)
class SecurityUiPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    @Test fun realLockedEnglishVietnameseSecurePixels() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val observation = SecurityUiObservation(activity)
        try {
            ui.waitForIdle()
            observation.foreground()
            if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
            for (vietnamese in listOf(false, true)) {
                if (vietnamese) ui.onNodeWithText("VI").performClick()
                ui.waitForIdle()
                val language = if (vietnamese) "vi" else "en"
                observation.report("$language-pixels-language-geometry")
                observation.measure(ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).fetchSemanticsNode())
                ui.onNodeWithText(if (vietnamese) "Tạo mật khẩu chính" else "Create your master password").assertExists()
                val master = if (vietnamese) "Mật khẩu chính" else "Master password"
                val field = ui.onNode(hasSetTextAction() and hasText(master)).scrollIfAble()
                ui.waitForIdle()
                field.assertIsDisplayed().assertIsEnabled().assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
                observation.report("$language-pixels-field-accessibility-diagnostics")
                observation.fieldAccessibility(field.fetchSemanticsNode(), master)
                observation.report("$language-independent-secure-pixels")
                observation.capture()
            }
            observation.foreground()
            assertFalse("Locked pixels fixture must create no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
            observation.report("locked-security-pixels-passed", mapOf("passed" to true))
        } catch (error: Throwable) {
            runCatching { observation.failure(error) }
            throw AssertionError("Security pixels acceptance failed at ${observation.phase}; safe metadata recorded")
        } finally {
            ui.activityRule.scenario.close()
            directory.deleteRecursively()
        }
    }
    @Test fun realLockedEnglishVietnameseSecureDisplayCapture() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val observation = SecurityUiObservation(activity)
        try {
            ui.waitForIdle()
            observation.foreground()
            if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
            for (vietnamese in listOf(false, true)) {
                if (vietnamese) ui.onNodeWithText("VI").performClick()
                ui.waitForIdle()
                val language = if (vietnamese) "vi" else "en"
                observation.report("$language-capture-language-geometry")
                observation.measure(ui.onNode(hasClickAction() and hasText(if (vietnamese) "EN" else "VI")).fetchSemanticsNode())
                ui.onNodeWithText(if (vietnamese) "Tạo mật khẩu chính" else "Create your master password").assertExists()
                val master = if (vietnamese) "Mật khẩu chính" else "Master password"
                val field = ui.onNode(hasSetTextAction() and hasText(master)).scrollIfAble()
                ui.waitForIdle()
                field.assertIsDisplayed().assertIsEnabled().assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
                observation.report("$language-capture-password-accessibility")
                observation.accessibility(master)
                observation.report("$language-independent-secure-display-capture")
                observation.capture()
            }
            observation.foreground()
            assertFalse("Locked capture fixture must create no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
            observation.report("locked-security-capture-passed", mapOf("passed" to true))
        } catch (error: Throwable) {
            runCatching { observation.failure(error) }
            throw AssertionError("Security capture acceptance failed at ${observation.phase}; safe metadata recorded")
        } finally {
            ui.activityRule.scenario.close()
            directory.deleteRecursively()
        }
    }
    @Test fun realLockedEnglishVietnameseSemanticsLayoutAndSecureCapture() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val observation = SecurityUiObservation(activity)
        try {
            ui.waitForIdle()
            observation.foreground()
            if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
            for (vietnamese in listOf(false, true)) {
                if (vietnamese) ui.onNodeWithText("VI").performClick()
                ui.waitForIdle()
                val language = if (vietnamese) "vi" else "en"
                observation.report("$language-language-target")
                observation.target(ui.onNodeWithText(if (vietnamese) "EN" else "VI").fetchSemanticsNode())
                val master = if (vietnamese) "Mật khẩu chính" else "Master password"
                // Redesign SU1: master password + confirmation (secret parts are step 2).
                val fields = listOf(master, if (vietnamese) "Nhập lại" else "Type it again")
                fields.forEachIndexed { index, label ->
                    observation.report("$language-password-field-$index")
                    val field = ui.onNode(hasSetTextAction() and hasText(label)).scrollIfAble()
                    ui.waitForIdle()
                    field.assertIsDisplayed().assertIsEnabled().assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
                    observation.target(field.fetchSemanticsNode())
                    if (index == 0) observation.accessibility(master)
                }
                // SU1 has one action (disabled until both fields match and the acknowledgement is ticked).
                val buttons = if (vietnamese) listOf("Tiếp") else listOf("Next")
                buttons.forEachIndexed { index, label ->
                    observation.report("$language-button-$index")
                    val button = ui.onNode(hasClickAction() and hasText(label)).scrollIfAble()
                    ui.waitForIdle()
                    button.assertIsDisplayed()
                    if (index == 0) button.assertIsNotEnabled() else button.assertIsEnabled()
                    observation.target(button.fetchSemanticsNode())
                }
                // Capture only this actual locked proof screen, with no fields populated.
                ui.onNode(hasSetTextAction() and hasText(master)).scrollIfAble()
                ui.waitForIdle()
                observation.report("$language-secure-display-capture")
                observation.capture()
            }
            observation.report("locked-security-ui-passed", mapOf("passed" to true))
            observation.foreground()
            assertFalse("Locked security fixture must create no encrypted Cookbook", File(directory, "cookbook-v1.json").exists())
        } catch (error: Throwable) {
            runCatching { observation.failure(error) }
            throw AssertionError("Security/UI acceptance failed at ${observation.phase}; safe metadata recorded")
        } finally {
            ui.activityRule.scenario.close()
            directory.deleteRecursively()
        }
    }
}

package dev.passchef.mobile.proof

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.cookbook.CookbookController
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Genuine owner setup; selected EN/VI Cook/revealed capture only. No Recents/TalkBack claim. */
@RunWith(AndroidJUnit4::class)
class AuthenticatedSecurityUiPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private var phase = "authenticated-security-start"
    private fun report(name: String, flags: Map<String, Boolean> = emptyMap(), counts: Map<String, Int> = emptyMap()) {
        phase = name
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefAuthenticatedSecurityPhase", name)
            flags.forEach { (key, value) -> putBoolean("passchefAuthenticatedSecurity_$key", value) }
            counts.forEach { (key, value) -> putInt("passchefAuthenticatedSecurity_$key", value) }
        })
    }
    private fun book() = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }
        .get(ui.activity) as CookbookController
    private fun ready(proof: CookbookTimerFixture, requireRevealed: Boolean = false) {
        proof.environment()
        val flags = timerMain {
            CookbookUiDiagnostics.stateFlags(ui.activity) + mapOf(
                "secureFlag" to (ui.activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
                "recordPresent" to File(ui.activity.proofDirectory, "cookbook-v1.json").exists(),
                "exactSyntheticDish" to (book().dish == "aB!*abcd"))
        }
        report(phase, flags)
        assertTrue("Actual authenticated secure synthetic Dish remains ready", flags["secureFlag"] == true &&
            flags["recordPresent"] == true && flags["vaultUnlocked"] == true && flags["vaultReady"] == true &&
            flags["hasDish"] == true && (if (requireRevealed) flags["exactSyntheticDish"] == true && flags["revealed"] == true
            else flags["dishNonempty"] == false && flags["revealed"] == false))
    }
    private fun capture(proof: CookbookTimerFixture) {
        ready(proof, requireRevealed = true)
        ui.onNodeWithTag("dish-display").assertIsDisplayed()
        val dishNode = ui.onNodeWithTag("dish-display").fetchSemanticsNode()
        val rectangle = timerMain {
            Rect().also { rect ->
                val content = ui.activity.findViewById<View>(android.R.id.content)
                assertTrue("Actual authenticated content is visible", content.getLocalVisibleRect(rect))
                val offset = IntArray(2).also(content::getLocationOnScreen)
                rect.offset(offset[0], offset[1])
                val margin = (4 * ui.activity.resources.displayMetrics.density).toInt().coerceAtLeast(1)
                rect.inset(margin, margin)
            }
        }
        val dishVisibleInsideCapture = timerMain {
            val bounds = dishNode.boundsInWindow
            val offset = IntArray(2).also(ui.activity.window.decorView::getLocationOnScreen)
            bounds.width > 0 && bounds.height > 0 && bounds.left + offset[0] >= rectangle.left &&
                bounds.top + offset[1] >= rectangle.top && bounds.right + offset[0] <= rectangle.right &&
                bounds.bottom + offset[1] <= rectangle.bottom
        }
        report(phase, mapOf("revealedDishVisibleInsideCapture" to dishVisibleInsideCapture))
        assertTrue("Actual revealed Dish is visible inside sampled own content", dishVisibleInsideCapture)
        val bitmap = securityDisplayCapture("passchefAuthenticatedSecurity")
        assertTrue("Nonnull display capture required for authenticated secure exclusion", bitmap != null)
        try {
            val image = requireNotNull(bitmap)
            assertTrue("Real display capture covers actual authenticated own content", rectangle.width() > 0 && rectangle.height() > 0 &&
                rectangle.left >= 0 && rectangle.top >= 0 && rectangle.right <= image.width && rectangle.bottom <= image.height)
            val row = IntArray(rectangle.width())
            var pixels = 0; var black = 0
            for (y in rectangle.top until rectangle.bottom) {
                image.getPixels(row, 0, row.size, rectangle.left, y, row.size, 1)
                for (pixel in row) {
                    pixels++
                    if (Color.alpha(pixel) == 255 && Color.red(pixel) == 0 && Color.green(pixel) == 0 && Color.blue(pixel) == 0) black++
                }
            }
            report(phase, mapOf("captureAvailable" to true, "ownContentAllBlack" to (pixels > 0 && black == pixels)),
                mapOf("captureWidth" to image.width, "captureHeight" to image.height, "ownPixels" to pixels, "blackPixels" to black))
            assertTrue("Revealed authenticated own content is fully excluded from display capture", pixels > 0 && black == pixels)
        } finally { bitmap?.recycle() }
        ready(proof, requireRevealed = true)
        ui.onNodeWithTag("dish-display").assertIsDisplayed().assertTextEquals("aB!*abcd")
    }
    @Test fun realAuthenticatedEnglishVietnameseCookingAndSecureDisplayCapture() {
        val proof = CookbookTimerFixture(ui)
        try {
            // Reuse only genuine setup/Cook/UUID cleanup; no timer arm, deadline change or clock writes.
            proof.run("security-ui") {
                val file = File(ui.activity.proofDirectory, "cookbook-v1.json")
                val jars = file.readBytes()
                for (vietnamese in listOf(false, true)) {
                    val language = if (vietnamese) "vi" else "en"
                    report("$language-authenticated-ui")
                    if (vietnamese) {
                        ui.onNodeWithText("VI").performClick()
                        ui.waitForIdle()
                        ui.onNodeWithTag("dish-display").assertDoesNotExist()
                        ui.onNodeWithTag("dish-copy").assertDoesNotExist()
                        ui.onNode(hasSetTextAction() and hasText("Dán Recipe")).assertExists()
                        ui.onNode(hasClickAction() and hasText("Nấu Dish")).performScrollTo().performClick()
                        ui.waitUntil(10_000) { timerMain { book().hasDish() } }
                    }
                    ready(proof)
                    ui.onNodeWithTag("manual-recipe").assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText))
                    ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
                    ui.onNodeWithTag("dish-copy").assertIsEnabled()
                    ui.onNodeWithTag("dish-reveal").assertTextEquals(if (vietnamese) "Hiện trong 10 giây" else "Reveal for 10 seconds")
                    ui.onNodeWithTag("dish-reveal").performScrollTo().performClick()
                    ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
                    assertTrue("Real explicit reveal is active before secure display capture", timerMain { book().revealed })
                    hideTimerIme(ui.activity) { proof.environment() }
                    ui.waitForIdle()
                    ui.onNodeWithTag("dish-display").performScrollTo().assertIsDisplayed()
                    ui.waitForIdle()
                    report("$language-authenticated-revealed-capture")
                    capture(proof)
                    assertTrue("Actual secure capture preserves encrypted proof jars", jars.contentEquals(file.readBytes()))
                }
                report("authenticated-security-ui-passed", mapOf("passed" to true))
            }
        } catch (failure: Throwable) {
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefAuthenticatedSecurityPhase", "$phase-failed")
                putString("passchefAuthenticatedSecurityFailureClass", failure.javaClass.simpleName)
            })
            throw AssertionError("Authenticated Security/UI acceptance failed at $phase; safe metadata recorded")
        }
    }
}

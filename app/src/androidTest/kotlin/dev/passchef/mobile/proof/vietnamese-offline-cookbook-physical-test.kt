package dev.passchef.mobile.proof

import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.recipe.TagCodec
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Selected physical VI navigation at actual 200%/landscape; genuine setup, no Google calls. */
@RunWith(AndroidJUnit4::class)
class VietnameseOfflineCookbookPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private fun click(label: String) = ui.onNode(hasClickAction() and hasText(label)).scrollIfAble().performClick()
    private fun present(tag: String) = ui.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
    private fun field(tag: String, value: String) = ui.onNodeWithTag(tag).scrollIfAble().performTextReplacement(value)
    private fun clip(): String? = (ui.activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .primaryClip?.takeIf { it.itemCount == 1 }?.getItemAt(0)?.text?.toString()
    private fun dish(expected: String) = ui.onNodeWithTag("dish-display").scrollIfAble().assertTextEquals(expected)
    private fun cook(recipe: String) {
        field("manual-recipe", recipe)
        ui.onNodeWithTag("manual-cook").scrollIfAble().performClick()
    }
    @Test fun realVietnameseSourceMappingDetailAndClipboardAtLargeLandscape() {
        val directory = ui.activity.proofDirectory
        val proof = CookbookTimerFixture(ui)
        proof.run("vi-offline-layout") {
            proof.environment()
            val configuration = timerMain { ui.activity.resources.configuration }
            assertTrue("Actual own 200% landscape required", configuration.fontScale == 2f &&
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            val jars = File(directory, "cookbook-v1.json").readBytes()
            RedesignUi.language(ui, vietnamese = true)
            ui.waitForIdle()
            ui.onNode(hasText("Nhập mã")).assertExists()
            ui.onNode(hasText("Cài đặt")).assertExists()
            RedesignUi.enterCode(ui)
            ui.onNodeWithTag("dish-display").assertDoesNotExist()
            cook("abcd#1")
            ui.waitUntil(10_000) { present("dish-display") }
            dish("••••••••")
            ui.onNodeWithTag("dish-reveal").scrollIfAble().assertTextEquals("Hiện 10 giây").performClick()
            dish("aB!*abcd")
            proof.report("vi-manual-reveal")
            Thread.sleep(10_500)
            proof.environment(); dish("••••••••")
            ui.onNodeWithTag("dish-copy").scrollIfAble().performClick()
            assertTrue("Actual VI Dish copy", clip() == "aB!*abcd")
            field("manual-recipe", "invalid Recipe")
            assertTrue("VI edit clears Dish and Copy", !present("dish-display") && !present("dish-copy"))
            ui.onNodeWithTag("manual-cook").scrollIfAble().performClick()
            ui.waitUntil(10_000) { present("cook-error") }
            assertTrue("Invalid Recipe has no Copy", !present("dish-copy"))
            val tag = TagCodec().compute(RecipeParser.parse("abcd#1")!!, "aB!*", "Menu_0123456789")
            cook("abcd#1.$tag")
            ui.waitUntil(10_000) { present("cook-error") }
            assertTrue("Tagged Recipe without Menu refuses Dish", !present("dish-display") && !present("dish-copy"))
            RedesignUi.offlineDraft(ui, { RedesignUi.book(ui.activity) }, dev.passchef.mobile.cookbook.MenuOrigin("Menu_0123456789", 0, "Menu ngoại tuyến"),
                "Label\tRecipe\tUsername\nMail\tabcd#1\t00123\nBank\tdef#1\té")
            proof.report("vi-source-column-preview")
            ui.onNode(hasSetTextAction() and hasText("Nâng cao: dữ liệu bắt đầu từ hàng"))
                .scrollIfAble().performTextReplacement("2")
            for ((role, column) in mapOf("RECIPE" to "2", "LABEL" to "1", "USERNAME" to "3", "URL" to "", "NOTES" to "")) {
                field("column-$role", column)
            }
            hideTimerIme(ui.activity, proof::environment)
            ui.onNodeWithTag("confirm-columns").scrollIfAble().assertTextEquals("Đúng rồi — Xem lại")
            RedesignUi.confirmAndSave(ui)
            ui.waitUntil(10_000) { present("menu-search") }
            RedesignUi.enterCode(ui); click("Menu ngoại tuyến")
            cook("abcd#1.$tag")
            ui.waitUntil(10_000) { present("dish-display") }; dish("••••••••")
            val wrongTag = if (tag == "aaaa") "bbbb" else "aaaa"
            field("manual-recipe", "abcd#1.$wrongTag")
            assertTrue("Editing tagged masked Dish clears both actions", !present("dish-display") && !present("dish-copy"))
            ui.onNodeWithTag("manual-cook").scrollIfAble().performClick()
            ui.waitUntil(10_000) { present("cook-error") }
            assertTrue("Wrong verification tag refuses Dish", !present("dish-display") && !present("dish-copy"))
            RedesignUi.passwords(ui)
            ui.onNode(hasSetTextAction() and hasText("Tìm tên, tài khoản, trang web, ghi chú")).assertExists()
            field("menu-search", "0012")
            click("Mail")
            ui.waitUntil(10_000) { present("dish-display") }; dish("••••••••")
            ui.onNodeWithTag("dish-reveal").scrollIfAble().performClick(); dish("aB!*abcd")
            ui.onNodeWithTag("dish-copy").scrollIfAble().performClick()
            assertTrue("Cached VI Dish copy", clip() == "aB!*abcd")
            click("Chép")
            assertTrue("Literal username", clip() == "00123")
            proof.report("vi-cached-detail-username-timer")
            Thread.sleep(30_500)
            proof.environment()
            assertTrue("Original Dish timer preserves username", clip() == "00123")
            RedesignUi.backFromDetail(ui)
            assertTrue("VI Back clears Dish", !present("dish-display") && !present("dish-copy"))
            assertTrue("Encrypted jars preserved", jars.contentEquals(File(directory, "cookbook-v1.json").readBytes()))
            proof.report("vi-offline-controls-verified")
            ui.activityRule.scenario.moveToState(Lifecycle.State.CREATED)
            ui.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
            ui.waitUntil(10_000) { ui.onAllNodesWithTag("unlock-biometric").fetchSemanticsNodes().isNotEmpty() }
            assertTrue("Actual background locks and clears detail/list", !present("dish-display") && !present("menu-search") && !present("nav-menu"))
        }
        assertTrue("Only this UUID fixture removed", !directory.exists())
    }
}

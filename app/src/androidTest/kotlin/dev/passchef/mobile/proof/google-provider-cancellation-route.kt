package dev.passchef.mobile.proof

import android.app.Activity
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import java.io.File
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue

fun providerCancelControl(file: File): JSONObject {
    require(file.isFile && file.canonicalFile == file)
    val bytes = ByteArray(513)
    val count = file.inputStream().use { input ->
        var size = 0
        while (size < bytes.size) {
            val n = input.read(bytes, size, bytes.size - size); if (n < 0) break; if (n > 0) size += n
        }; size
    }
    require(count in 1..512)
    return JSONObject(String(bytes, 0, count, Charsets.UTF_8))
}

/** Root selects only a naturally available existing task route; native never drives external Google UI. */
class GoogleProviderCancellationRoute(private val ui: ComposeTestRule,
    private val observed: GoogleProviderCancellationObservation, private val approval: String,
    private val witness: GoogleResultProofWitness) {
    private val state = observed.state
    private fun report(phase: String) = observed.report(phase, witness)
    private fun click(label: String) {
        assertTrue("Actual own foreground required before normal UI click", state.foreground())
        ui.onNode(hasClickAction() and hasText(label)).performScrollTo().performClick()
    }
    private fun route(): String {
        val marker = File(observed.activity.proofDirectory, "google-provider-cancel-route.json")
        assertTrue("No preexisting route can decide the fresh pending request", !marker.exists())
        val deadline = SystemClock.elapsedRealtime() + 120_000
        report("await-root-existing-task-route")
        while (!marker.exists() && SystemClock.elapsedRealtime() < deadline) {
            assertTrue("Original actual SDK request remains pending before route selection", observed.pending())
            observed.assertPreserved(); Thread.sleep(100)
        }
        require(SystemClock.elapsedRealtime() < deadline)
        val data = providerCancelControl(marker)
        require(data.keys().asSequence().toSet() == setOf("fixtureId", "approvalRecordSha256", "actionRecordSha256",
            "mode", "ownTaskId", "googleTaskId"))
        val mode = data.getString("mode")
        require(data.getString("fixtureId") == observed.activity.proofId && data.getString("approvalRecordSha256") == approval &&
            data.getString("actionRecordSha256") == observed.action && mode in setOf("cancel-only", "switch-source") &&
            data.getInt("ownTaskId") == observed.activity.taskId && data.getInt("googleTaskId") > 0)
        if (mode == "switch-source") require(data.getInt("googleTaskId") != observed.activity.taskId)
        assertTrue("Only the current original pending request can receive the bounded route", observed.pending())
        return mode
    }
    private fun previewDifferent() {
        report("await-own-existing-task-focus")
        state.await { state.foreground() && observed.pending() }
        observed.assertPreserved()
        assertTrue("Existing-task return is locked before genuine biometric reopen", state.flags()["ready"] == false)
        report("owner-reopen-biometric-for-local-preview")
        click("Open with biometric")
        state.await { state.foreground() && state.flags()["ready"] == true && state.flags()["sourceBusy"] == false }
        ui.waitForIdle(); observed.assertPreserved()
        assertTrue("Genuine reopen leaves original SDK request pending and jars unchanged", observed.pending())
        click("Sources")
        val id = "proof-switch-" + observed.activity.proofId.replace("-", "")
        val title = "Draft-B"
        state.requireReady(); ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(id)
        state.requireReady(); ui.onNode(hasSetTextAction() and hasText("Local tab label"))
            .performScrollTo().performTextReplacement(title)
        state.requireReady(); ui.onNodeWithTag("source-table").performScrollTo()
            .performTextReplacement("Label\tRecipe\tUsername\nLocal-B\tabcd#1\t00123")
        click("Preview columns")
        state.await { state.foreground() && state.flags()["ready"] == true && state.flags()["sourceBusy"] == false }
        ui.waitForIdle(); observed.assertDifferentDraft(id, title)
        report("different-B-preview-original-pending")
        // Root focuses only the previously recorded existing Google task, then verifies real Google focus again.
        state.await { !state.foreground() && observed.pending() && observed.observeProvider() }
    }
    fun run() {
        val externalDeadline = SystemClock.elapsedRealtime() + 30_000
        var external = false
        while (SystemClock.elapsedRealtime() < externalDeadline) {
            if (observed.observeProvider()) { external = true; break }
            if (state.foreground() && timerMain { !state.book().sources.busy && !observed.pending() }) break
            Thread.sleep(50)
        }
        if (!external) {
            observed.assertPreserved()
            val idle = timerMain(witness::snapshot)
            assertTrue("Only no-stop/no-request/zero-witness silent flow can be NotExercised",
                observed.noResolutionObserved() && timerMain { !state.book().sources.busy } && idle.launches == 0 &&
                    idle.results == 0 && idle.forwarded == 0 && idle.requestCode == null && idle.resultCode == null &&
                    !idle.matchedOriginal && !idle.originalStillPending && !idle.consumed && !idle.failed)
            report("not-exercised-no-real-external-google")
            assumeTrue("Actual401 prelude is distinct; no external Google UI is not human cancellation", false)
        }
        val pending = timerMain(witness::snapshot)
        assertTrue("One real IntentSender launch binds the original pending request and actual registry code",
            pending.launches == 1 && pending.requestCode != null && pending.results == 0 && pending.forwarded == 0 &&
                pending.originalStillPending && !pending.matchedOriginal && !pending.consumed && !pending.failed)
        if (route() == "switch-source") previewDifferent()
        report("owner-cancel-real-google-now")
        val deadline = SystemClock.elapsedRealtime() + 180_000
        while (SystemClock.elapsedRealtime() < deadline && (!state.foreground() || observed.pending())) {
            observed.assertPreserved(); Thread.sleep(100)
        }
        assertTrue("Actual owner cancellation returns and consumes the real request within bounded wait", state.foreground() && !observed.pending())
        ui.waitForIdle(); observed.assertReturned()
        val result = timerMain(witness::snapshot)
        assertTrue("Real canceled result follows unchanged normal registry forwarding exactly once",
            result.launches == 1 && result.results == 1 && result.forwarded == 1 && result.requestCode == pending.requestCode &&
                result.resultCode == Activity.RESULT_CANCELED && result.matchedOriginal && result.consumed &&
                !result.originalStillPending && !result.failed)
        ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
        report("provider-cache-real-canceled-result-passed")
    }
}

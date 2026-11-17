package dev.passchef.mobile.proof

import android.app.ActivityManager
import android.app.ApplicationExitInfo
import android.os.Bundle
import android.os.Process
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Read-only own-package evidence; a missing/ambiguous record earns no low-memory claim. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 36)
class BackgroundExitReasonTest {
    @Test fun observeOnlyOriginalWarmPidExitInRecordedPhoneTimeWindow() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefSavedTaskStage") == "background-exit-reason")
        val context = instrumentation.targetContext
        val baseline = SavedTaskBaseline.read(context.noBackupFilesDir,
            requireNotNull(args.getString("passchefSavedTaskFixtureId")))
        val expectedPid = requireNotNull(args.getString("passchefSavedTaskExpectedPid")).toInt()
        assertTrue("Observer attaches to the actual naturally new process", Process.myPid() == expectedPid && expectedPid != baseline.pid)
        val start = requireNotNull(args.getString("passchefBackgroundStartEpochMs")).toLong()
        val end = requireNotNull(args.getString("passchefBackgroundEndEpochMs")).toLong()
        require(start > 0L && end >= start && end - start <= 900_000L && end <= System.currentTimeMillis())
        val matches = context.getSystemService(ActivityManager::class.java)
            .getHistoricalProcessExitReasons(context.packageName, baseline.pid, 16)
            .filter { it.pid == baseline.pid && it.processName == context.packageName && it.timestamp in start..end }
        val one = matches.singleOrNull()
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefBackgroundExitFixture", baseline.directory.name)
            putInt("passchefBackgroundExitCurrentPid", Process.myPid())
            putInt("passchefBackgroundExitOriginalPid", baseline.pid)
            putInt("passchefBackgroundExitTaskId", baseline.taskId)
            putLong("passchefBackgroundExitWindowStart", start)
            putLong("passchefBackgroundExitWindowEnd", end)
            putInt("passchefBackgroundExitMatches", matches.size)
            putLong("passchefBackgroundExitTimestamp", one?.timestamp ?: -1L)
            putInt("passchefBackgroundExitReason", one?.reason ?: -1)
            putBoolean("passchefBackgroundExitLowMemory", one?.reason == ApplicationExitInfo.REASON_LOW_MEMORY)
        })
        // No description, process-state summary, trace, token, Activity launch or cache write.
        // JUnit pass means observation completed; only exact matched low-memory metadata can qualify that cause.
    }
}

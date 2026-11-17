package dev.passchef.mobile.proof

import android.app.ActivityManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Host force-stops only the selected debug app AFTER recording restoration; never part of restore proof. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 36)
class SavedTaskCleanupTest {
    @Test fun removeOnlyTheRecordedSavedTaskFixtureAndItsAliases() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefSavedTaskStage") == "cleanup")
        val context = instrumentation.targetContext
        val baseline = SavedTaskBaseline.read(context.noBackupFilesDir,
            requireNotNull(args.getString("passchefSavedTaskFixtureId")))
        assertTrue("Cleanup is bound to this run's original process", baseline.pid ==
            requireNotNull(args.getString("passchefSavedTaskWarmPid")).toInt())
        assertTrue("Host stopped the selected task before file cleanup", context.getSystemService(ActivityManager::class.java)
            .appTasks.none { it.taskInfo.taskId == baseline.taskId && it.taskInfo.numActivities > 0 })
        val failures = mutableListOf<Throwable>()
        fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
        var aliases = emptyList<String>()
        clean { VaultRepository(AtomicRecordStore(File(baseline.directory, "cookbook-v1.json"))).record()?.let {
            aliases = (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty).distinct()
        } }
        aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
        clean { assertTrue("Only the exact canonical task proof directory is removed", baseline.directory.deleteRecursively()) }
        if (failures.isNotEmpty()) throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
        baseline.report("saved-task-owned-cleanup-passed")
    }
}

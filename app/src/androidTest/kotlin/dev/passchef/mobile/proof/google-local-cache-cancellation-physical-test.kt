package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.os.Looper
import android.os.PowerManager
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.auth.api.identity.ClearTokenRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Tasks
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.AndroidSheetsAuthorization
import dev.passchef.mobile.cookbook.SourceOwnership
import dev.passchef.mobile.cookbook.CookbookSources
import dev.passchef.mobile.cookbook.menuIdFromInput
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** App-local SDK cache operation only; no remote revocation, fabricated401 or account information. */
@RunWith(AndroidJUnit4::class)
class GoogleLocalCacheCancellationPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun localSdkCacheClearThenActualGoogleCancellationPreservesProviderCookbook() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefLocalSdkCacheClearApproved") == "true")
        val id = requireNotNull(args.getString("passchefLocalCacheFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val approval = requireNotNull(args.getString("passchefLocalCacheApprovalSha256"))
        require(approval.matches(Regex("[a-f0-9]{64}")))
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var observation: GoogleProviderCancellationObservation? = null
        var primary: Throwable? = null
        var phase = "local-provider-setup"
        var operation: ByteArray? = null
        var clearCompleted = false
        var clearSuccessful = false
        var taskId: Int? = null
        val aliases = mutableSetOf<String>()
        fun report(name: String, complete: Boolean = clearCompleted, successful: Boolean = clearSuccessful) {
            phase = name
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefLocalSdkPhase", name); putString("passchefLocalSdkFixtureId", id)
                putString("passchefLocalSdkApprovalSha256", approval); putInt("passchefLocalSdkPid", Process.myPid())
                taskId?.let { putInt("passchefLocalSdkTaskId", it) }
                putString("passchefLocalSdkKind", "LocalSdkTokenCacheClear")
                putBoolean("passchefLocalSdkCompleted", complete); putBoolean("passchefLocalSdkSuccessful", successful)
                putBoolean("passchefLocalSdkRemoteRevocationPerformed", false)
                operation?.let { putString("passchefLocalSdkOperationSha256", googleColdHash(it))
                    putString("passchefLocalSdkOperationJson", it.toString(Charsets.UTF_8)) }
            })
        }
        fun files(): Map<String, String> {
            val root = directory.canonicalFile
            return root.walkTopDown().onEnter { child ->
                require(child.canonicalFile == child && child.toPath().startsWith(root.toPath())); true
            }.filter { it.isFile }.map { file ->
                require(file.canonicalFile == file && file.toPath().startsWith(root.toPath()))
                file.relativeTo(root).invariantSeparatorsPath to googleColdHash(file.readBytes())
            }.toMap().also { require(it.size <= 32) }
        }
        try {
            val launched = ActivityScenario.launch<ColdStartProofActivity>(Intent(instrumentation.targetContext, ColdStartProofActivity::class.java)
                .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            scenario = launched
            lateinit var activity: ColdStartProofActivity
            launched.onActivity { activity = it; taskId = it.taskId }
            val state = ColdCacheObservation(activity)
            assertTrue("No diagnostic witness is armed during initial ordinary provider authorization", activity.googleResultProof == null)
            val saved = GoogleColdProviderUi(ui, state) { report(it) }.load(url, "Test")
            val cached = GoogleColdCacheUi(ui, state) { report(it) }.favoriteCook(saved)
            ui.waitForIdle(); state.requireReady()
            assertTrue("Actual provider A writes finish before freezing jars/cache", state.flags()["sourceBusy"] == false)
            val baseline = GoogleColdBaseline.create(activity, cached)
            val before = files()
            fun owned(): List<Any?> {
                require(Looper.myLooper() == Looper.getMainLooper())
                val vault = state.vault(); val source = state.book().sources; val decor = activity.window.decorView
                val session = VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault)
                val owner = CookbookSources::class.java.getDeclaredField("ownership").apply { isAccessible = true }.get(source)
                val generation = SourceOwnership::class.java.getDeclaredField("generation").apply { isAccessible = true }.getLong(owner)
                val context = SourceOwnership::class.java.getDeclaredField("source").apply { isAccessible = true }.get(owner)
                val token = CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }.get(source) as? String
                val pending = AndroidSheetsAuthorization::class.java.getDeclaredField("pending").apply { isAccessible = true }.get(state.book().authorization)
                val keyguard = activity.getSystemService(KeyguardManager::class.java)
                val foreground = activity.lifecycle.currentState == Lifecycle.State.RESUMED && decor.hasWindowFocus() &&
                    decor.isAttachedToWindow && decor.isShown && activity.getSystemService(PowerManager::class.java).isInteractive &&
                    !keyguard.isKeyguardLocked && !keyguard.isDeviceLocked &&
                    (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE) != 0
                assertTrue("Actual secure owned A is ready with session/grant/metadata, exact context and no busy/pending request",
                    foreground && vault.readyForCooking() && vault.unlocked && !vault.busy && session != null && !token.isNullOrBlank() &&
                        pending == null && !source.busy && source.draft == null && context == cached.mapping.origin.key &&
                        source.snapshots == listOf(cached) && source.metadata?.spreadsheetId == cached.mapping.origin.menuId &&
                        source.metadata?.sheets?.singleOrNull { it.properties.title == "Test" }?.properties?.sheetId == cached.mapping.origin.tabId)
                return listOf(session, generation, context, token)
            }
            val original = timerMain { owned() }
            fun sameOwned() {
                val current = owned()
                assertTrue("Original session/source generation/context/opaque grant remain exact across the local SDK operation",
                    current[0] === original[0] && current[1] == original[1] && current[2] == original[2] && current[3] === original[3])
            }
            report("actual-provider-A-frozen-before-local-operation")
            val started = System.currentTimeMillis()
            report("local-sdk-cache-clear-dispatch")
            val task = timerMain {
                sameOwned()
                Identity.getAuthorizationClient(activity).clearToken(ClearTokenRequest.builder().setToken(original[3] as String).build())
            }
            try { Tasks.await(task, 30, TimeUnit.SECONDS) }
            catch (_: Exception) {
                clearCompleted = task.isComplete; clearSuccessful = task.isSuccessful
                report("local-sdk-cache-clear-did-not-succeed")
                throw AssertionError("Bounded local SDK cache Task did not succeed; no cancellation or cache-clear credit")
            }
            clearCompleted = task.isComplete; clearSuccessful = task.isSuccessful
            assertTrue("Actual local SDK cache Task completed successfully", clearCompleted && clearSuccessful && !task.isCanceled)
            operation = JSONObject().put("version", 1).put("kind", "LocalSdkTokenCacheClear")
                .put("fixtureId", id).put("pid", Process.myPid()).put("taskId", requireNotNull(taskId))
                .put("approvalRecordSha256", approval).put("completed", true).put("successful", true)
                .put("startedAtEpochMs", started).put("completedAtEpochMs", System.currentTimeMillis())
                .put("remoteRevocationPerformed", false).toString().toByteArray(Charsets.UTF_8)
            report("local-sdk-cache-clear-completed", true, true) // Actual Task truth precedes every later ownership/file assertion.
            val record = File(directory, "google-local-sdk-cache-clear.json")
            require(!record.exists())
            AtomicRecordStore(record).write(requireNotNull(operation))
            assertTrue("Metadata-only operation record is stored exactly", record.readBytes().contentEquals(requireNotNull(operation)))
            timerMain { sameOwned() }
            val after = files()
            assertTrue("Local SDK operation preserves all prior provider-A bytes, adding only the typed operation record",
                after.keys == before.keys + record.name && before.all { (name, hash) -> after[name] == hash } &&
                    after[record.name] == googleColdHash(requireNotNull(operation)) && baseline.matches(directory))
            val localOperationSha = googleColdHash(requireNotNull(operation))
            val observed = GoogleProviderCancellationObservation(state, baseline, approval, localOperationSha)
            observation = observed
            val witness = observed.armWitness()
            state.requireReady(); ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            state.requireReady(); ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(url)
            ColdCacheUiFixture(ui, state).hideIme()
            observed.assertPreserved(); observed.report("normal-connect-dispatch-after-local-sdk-cache-clear", witness)
            report("normal-connect-after-local-operation", true, true)
            state.requireReady(); ui.onNode(hasClickAction() and hasText("Connect / load tabs")).performScrollTo().performClick()
            GoogleProviderCancellationRoute(ui, observed, approval, witness).run()
            report("local-sdk-provider-cache-cancellation-native-passed", true, true)
        } catch (failure: Throwable) {
            primary = failure
            runCatching { report("${observation?.phase ?: phase}-terminal-${failure.javaClass.simpleName}") }
            throw failure
        } finally {
            val failures = mutableListOf<Throwable>()
            fun clean(work: () -> Unit) { try { work() } catch (failure: Throwable) { failures += failure } }
            clean { observation?.close() }; clean { scenario?.close() }
            clean { VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases += (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty)
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { assertTrue("Only this canonical local-operation fixture is removed", !directory.exists() || directory.deleteRecursively()) }
            clean { assertTrue("Local-operation fixture removal is verified", !directory.exists()) }
            if (failures.isNotEmpty()) {
                val original = primary
                if (original != null) failures.forEach(original::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}

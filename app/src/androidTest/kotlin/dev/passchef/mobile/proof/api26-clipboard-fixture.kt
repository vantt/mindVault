package dev.passchef.mobile.proof

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.PowerManager
import android.os.Process
import android.os.SystemClock
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.ClipboardOwnership
import dev.passchef.mobile.cookbook.OwnedClipboard
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import java.io.File
import java.util.UUID

data class Api26ClipboardClaim(val label: String?, val deadline: Long, val generation: Long, val valuePresent: Boolean)

/** Locked real adapter only. Private policy fields are observed, never changed. */
class Api26ClipboardFixture {
    val id = UUID.randomUUID().toString()
    private val base = InstrumentationRegistry.getInstrumentation().targetContext.noBackupFilesDir.canonicalFile
    private val directory = File(base, "cookbook-cold-proof-$id").canonicalFile
    private var scenario: ActivityScenario<ColdStartProofActivity>? = null
    private var observation: ColdCacheObservation? = null
    private var ownsDirectory = false
    private val labels = mutableMapOf<String, String>()
    private var nullReplacement: String? = null
    private val extraKey = "dev.passchef.proof.API26_OWNER"
    private fun read(owner: Any, field: String) = owner.javaClass.getDeclaredField(field)
        .apply { isAccessible = true }.get(owner)
    private fun foreground(activity: ColdStartProofActivity): Boolean {
        val decor = activity.window.decorView
        val keyguard = activity.getSystemService(KeyguardManager::class.java)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
            activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0 &&
            decor.hasWindowFocus() && decor.isAttachedToWindow && decor.isShown &&
            activity.getSystemService(PowerManager::class.java).isInteractive &&
            !keyguard.isKeyguardLocked && !keyguard.isDeviceLocked
    }
    private fun guard(activity: ColdStartProofActivity) {
        assertTrue("Actual secure locked own foreground required", foreground(activity))
        assertTrue("Clipboard proof creates no encrypted Cookbook", !File(directory, "cookbook-v1.json").exists())
        assertTrue("Only the canonical owned UUID is observed", activity.proofDirectory.canonicalFile == directory)
    }
    private fun <T> main(action: (ColdStartProofActivity, ClipboardManager) -> T): T {
        // ColdCacheObservation marshals internally; keep its full locked/session/token guard off main.
        requireNotNull(observation).initialLocked()
        var result: Result<T>? = null
        requireNotNull(scenario).onActivity { activity -> result = runCatching {
            guard(activity); action(activity, activity.getSystemService(ClipboardManager::class.java))
        } }
        return requireNotNull(result).getOrThrow()
    }
    fun open() {
        require(Build.VERSION.SDK_INT in 26..27)
        require(directory.parentFile == base && directory.name == "cookbook-cold-proof-$id" && !directory.exists())
        ownsDirectory = true
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        scenario = ActivityScenario.launch(Intent(context, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        requireNotNull(scenario).onActivity { observation = ColdCacheObservation(it) }
        val end = SystemClock.elapsedRealtime() + 10_000
        var ready = false
        while (!ready && SystemClock.elapsedRealtime() < end) {
            ready = requireNotNull(observation).foreground()
            if (!ready) Thread.sleep(100)
        }
        assertTrue("Real locked foreground became available", ready)
        main { _, _ -> }; report("api26-locked-ready")
    }
    fun claim() = main { activity, _ ->
        val adapter = OwnedClipboard.obtain(activity)
        val policy = read(adapter, "policy") as ClipboardOwnership
        Api26ClipboardClaim(read(adapter, "label") as String?, read(policy, "deadline") as Long,
            policy.token(), read(policy, "value") != null)
    }
    fun copyDish(suffix: String): Api26ClipboardClaim {
        val text = "SYNTHETIC_API26_${id}_$suffix"
        val prior = claim().generation
        val before = SystemClock.elapsedRealtime()
        main { activity, _ -> assertTrue("Actual adapter Copy succeeds", OwnedClipboard.obtain(activity).copyDish(text)) }
        val after = SystemClock.elapsedRealtime()
        val current = claim()
        val label = requireNotNull(current.label)
        assertTrue("One Copy owns a fresh original thirty-second deadline", label.startsWith("PassChef Dish ") &&
            current.generation == prior + 1 && current.deadline in before + 30_000..after + 30_000 && current.valuePresent)
        labels[label] = text; matches(label)
        main { _, manager -> assertTrue("Submitted sensitive metadata survives the real write",
            manager.primaryClip?.description?.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true) }
        report("api26-original-copy", current)
        return current
    }
    fun matches(label: String?) = main { _, manager ->
        val clip = requireNotNull(manager.primaryClip)
        assertTrue("Expected owned synthetic label and item count", clip.description.label?.toString() == label && clip.itemCount == 1)
        val expected = if (label == null) {
            assertTrue("Null-label content is read only behind this run's private synthetic marker",
                clip.description.extras?.getString(extraKey) == id)
            requireNotNull(nullReplacement)
        } else requireNotNull(labels[label])
        assertTrue("Exact owned synthetic content remains", clip.getItemAt(0).text?.toString() == expected)
    }
    fun replace(nullLabel: Boolean): String? {
        val label = if (nullLabel) null else "PassChef API26 replacement $id"
        val text = "SYNTHETIC_API26_REPLACEMENT_${id}_$nullLabel"
        if (label == null) nullReplacement = text else labels[label] = text
        main { _, manager ->
            val clip = ClipData.newPlainText(label, text)
            clip.description.extras = PersistableBundle().apply { putString(extraKey, id) }
            manager.setPrimaryClip(clip)
        }
        matches(label); return label
    }
    fun username() {
        val text = "SYNTHETIC_API26_USERNAME_$id"
        main { activity, _ -> assertTrue("Actual username Copy succeeds", OwnedClipboard.obtain(activity).copyUsername(text)) }
        labels["PassChef username"] = text; matches("PassChef username")
    }
    fun until(deadline: Long) {
        while (SystemClock.elapsedRealtime() < deadline) {
            main { _, _ -> }
            Thread.sleep(minOf(100L, (deadline - SystemClock.elapsedRealtime()).coerceAtLeast(1)))
        }
    }
    fun forgotten(generation: Long) {
        val current = claim()
        assertTrue("Production cleanup forgets only its owned claim", current.label == null && current.deadline == 0L &&
            current.generation == generation && !current.valuePresent)
    }
    fun emptyFallback() = main { _, manager ->
        val clip = requireNotNull(manager.primaryClip)
        assertTrue("API26 fallback retains an empty-label one-item clip", clip.itemCount == 1 && clip.description.label?.toString() == "")
        assertTrue("API26 fallback removes the synthetic Dish text", clip.getItemAt(0).text?.toString() == "")
    }
    fun report(phase: String, claim: Api26ClipboardClaim? = null) {
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefApi26Phase", phase); putString("passchefApi26FixtureId", id)
            putInt("passchefApi26Pid", Process.myPid()); putInt("passchefApi26Api", Build.VERSION.SDK_INT)
            claim?.let { putLong("passchefApi26Deadline", it.deadline); putLong("passchefApi26Generation", it.generation) }
        })
    }
    fun close(primary: Throwable?) {
        val failures = mutableListOf<Throwable>()
        fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
        clean { scenario?.onActivity { activity -> if (foreground(activity)) {
            val manager = activity.getSystemService(ClipboardManager::class.java)
            val clip = manager.primaryClip
            val label = clip?.description?.label?.toString()
            val expected = labels[label] ?: nullReplacement?.takeIf { label == null && clip?.description?.extras?.getString(extraKey) == id }
            if (expected != null && clip != null && clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == expected)
                manager.setPrimaryClip(ClipData.newPlainText("", ""))
        } } }
        clean { scenario?.close() }
        if (ownsDirectory) {
            var aliases = emptyList<String>()
            clean { VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases = (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty).distinct()
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { require(directory.canonicalFile == directory && directory.parentFile == base)
                assertTrue("Only the exact owned UUID is removed", !directory.exists() || directory.deleteRecursively())
                assertTrue("Owned UUID absence is verified", !directory.exists()) }
        }
        if (failures.isEmpty() && ownsDirectory) clean { report("api26-owned-cleanup-passed") }
        if (failures.isNotEmpty()) {
            if (primary != null) failures.forEach(primary::addSuppressed)
            else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
        }
    }
}

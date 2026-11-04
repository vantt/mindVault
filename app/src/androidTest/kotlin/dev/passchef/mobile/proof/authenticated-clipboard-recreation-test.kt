package dev.passchef.mobile.proof

import android.content.ClipboardManager
import android.os.Process
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.ClipboardOwnership
import dev.passchef.mobile.cookbook.OwnedClipboard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Genuine password setup/reopening; real biometric prompts remain owner actions when available. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 28)
class AuthenticatedClipboardRecreationTest {
    @get:Rule val ui = createEmptyComposeRule()
    private fun waitUntil(time: Long) { (time - SystemClock.elapsedRealtime()).takeIf { it > 0 }?.let(Thread::sleep) }
    @Test fun authenticatedCopyRetainsOriginalDeadlineAcrossRecreation() {
        val scenario = ActivityScenario.launch(CookbookProofActivity::class.java)
        val fixture = AuthenticatedCookbookFixture(ui, scenario)
        fixture.attach()
        val directory = fixture.directory
        var marker = ""
        var primaryFailure: Throwable? = null
        try {
            fixture.report("fixture-created")
            val expected = fixture.setupCacheAndCook()
            val record = requireNotNull(fixture.record())
            val baseline = fixture.hashes()
            val original = fixture.activity
            val pid = Process.myPid()
            val adapter = timerMain { fixture.book().clipboard }
            val policy = OwnedClipboard::class.java.getDeclaredField("policy").apply { isAccessible = true }.get(adapter) as ClipboardOwnership
            val deadlineField = ClipboardOwnership::class.java.getDeclaredField("deadline").apply { isAccessible = true }
            val labelField = OwnedClipboard::class.java.getDeclaredField("label").apply { isAccessible = true }
            val valueField = ClipboardOwnership::class.java.getDeclaredField("value").apply { isAccessible = true }
            val priorGeneration = timerMain { policy.token() }
            val beforeCopy = SystemClock.elapsedRealtime()
            ui.onNodeWithTag("dish-copy").performScrollTo().performClick()
            val afterCopy = SystemClock.elapsedRealtime()
            val deadline = timerMain { deadlineField.getLong(policy) }
            val generation = timerMain { policy.token() }
            marker = timerMain { requireNotNull(labelField.get(adapter) as String?) }
            assertTrue("One actual UI Copy sets an original thirty-second deadline", marker.startsWith("PassChef Dish ") &&
                generation == priorGeneration + 1 && deadline >= beforeCopy + 30_000 && deadline <= afterCopy + 30_000)
            fun ownedClip() = timerMain {
                assertTrue("System clipboard reads require actual focused own Activity", fixture.foreground())
                val clip = fixture.activity.getSystemService(ClipboardManager::class.java).primaryClip
                assertTrue("Actual UI Copy retains its sensitive exact synthetic Dish", clip != null &&
                    clip.description.label?.toString() == marker && clip.itemCount == 1 &&
                    clip.getItemAt(0).text?.toString() == "aB!*abcd" &&
                    clip.description.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true)
            }
            ownedClip(); fixture.report("authenticated-copy-original-deadline", mapOf("deadline" to deadline, "generation" to generation))
            waitUntil(deadline - 25_000)
            scenario.recreate(); fixture.attach(); fixture.requireLocked()
            assertTrue("Actual replacement Activity retains only the owned storage locator", fixture.activity !== original &&
                original.lifecycle.currentState == Lifecycle.State.DESTROYED && Process.myPid() == pid && fixture.directory == directory)
            fixture.requireCleared(original)
            assertTrue("Recreation preserves exact encrypted jars and confirmed cache", fixture.hashes() == baseline)
            assertTrue("Replacement foreground is observed before the original deadline", SystemClock.elapsedRealtime() < deadline)
            assertTrue("Same-process adapter/deadline/generation are unchanged", timerMain {
                fixture.book().clipboard === adapter && deadlineField.getLong(policy) == deadline &&
                    policy.token() == generation && labelField.get(adapter) == marker && valueField.get(policy) != null
            })
            ownedClip(); fixture.report("recreated-locked-original-copy", mapOf("deadline" to deadline, "generation" to generation))
            waitUntil(deadline + 500)
            fixture.requireLocked()
            assertTrue("Original cleanup deadline clears actual clipboard after authenticated recreation", timerMain {
                !fixture.activity.getSystemService(ClipboardManager::class.java).hasPrimaryClip() &&
                    deadlineField.getLong(policy) == 0L && policy.token() == generation &&
                    labelField.get(adapter) == null && valueField.get(policy) == null
            })
            assertTrue("Clipboard cleanup changes no encrypted jars/cache", fixture.hashes() == baseline)
            fixture.report("original-clipboard-deadline-cleared", mapOf("originalDeadline" to deadline,
                "observedAt" to SystemClock.elapsedRealtime(), "generation" to generation))
            fixture.reopen(expected)
            val reopened = requireNotNull(fixture.record())
            assertTrue("Genuine password reopening preserves authenticated encrypted Spice payload",
                reopened.salt.contentEquals(record.salt) && reopened.cacheIv.contentEquals(record.cacheIv) && reopened.cache.contentEquals(record.cache))
            assertTrue("Fresh cached Cook does not copy a Dish without an action", timerMain {
                !fixture.activity.getSystemService(ClipboardManager::class.java).hasPrimaryClip()
            })
            fixture.report("authenticated-recreation-clipboard-passed")
        } catch (failure: Throwable) {
            primaryFailure = failure
            throw failure
        } finally {
            val cleanupFailures = mutableListOf<Throwable>()
            fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { cleanupFailures += failure } }
            clean { timerMain {
                if (marker.isNotEmpty() && fixture.foreground()) {
                    val manager = fixture.activity.getSystemService(ClipboardManager::class.java)
                    val clip = manager.primaryClip
                    if (clip?.description?.label?.toString() == marker && clip.itemCount == 1 &&
                        clip.getItemAt(0).text?.toString() == "aB!*abcd") manager.clearPrimaryClip()
                }
            } }
            clean { scenario.close() }
            var aliases = emptyList<String>()
            clean { fixture.record()?.let { aliases = (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty).distinct() } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { assertTrue("Only the exact synthetic Cookbook is removed", directory.deleteRecursively()) }
            if (cleanupFailures.isNotEmpty()) {
                val primary = primaryFailure
                if (primary != null) cleanupFailures.forEach(primary::addSuppressed)
                else throw cleanupFailures.first().also { first -> cleanupFailures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}

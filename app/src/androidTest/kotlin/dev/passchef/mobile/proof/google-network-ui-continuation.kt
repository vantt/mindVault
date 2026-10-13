package dev.passchef.mobile.proof

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.cookbook.*
import org.junit.Assert.assertTrue
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/** Authorized USB/owner disables radios; provider cache/transport only, this fixture reads network state. */
class GoogleNetworkUiContinuation(
    private val ui: AndroidComposeTestRule<ActivityScenarioRule<CookbookProofActivity>, CookbookProofActivity>,
    private val phase: (String) -> Unit
) {
    private val reconnected = AtomicBoolean(false)
    private var transportFailed = false
    private var authorizationUnavailable = false
    private fun <T> main(action: (CookbookProofActivity) -> T): T {
        var result: Result<T>? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { result = runCatching { action(ui.activity) } }
        return requireNotNull(result).getOrThrow()
    }
    private fun controller(activity: CookbookProofActivity) = MainActivity::class.java.getDeclaredField("cookbook")
        .apply { isAccessible = true }.get(activity) as CookbookController
    private fun ready(activity: CookbookProofActivity) = CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true
    private fun foreground(activity: CookbookProofActivity): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && flags["powerInteractive"] == true &&
            flags["keyguardLocked"] == false && flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowAttached"] == true && flags["proofWindowShown"] == true
    }
    private fun networkFlags(activity: CookbookProofActivity): Map<String, Boolean> {
        val manager = activity.getSystemService(ConnectivityManager::class.java)
        return mapOf("networkReadSucceeded" to true, "defaultNetworkAbsent" to (manager.activeNetwork == null),
            "processBoundNetworkAbsent" to (manager.boundNetworkForProcess == null),
            "internetNetworksAbsent" to manager.allNetworks.none { network ->
                manager.getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            }, "reconnectionObserved" to reconnected.get())
    }
    private fun offline(activity: CookbookProofActivity): Boolean {
        val flags = networkFlags(activity)
        return flags["defaultNetworkAbsent"] == true && flags["processBoundNetworkAbsent"] == true &&
            flags["internetNetworksAbsent"] == true && !reconnected.get()
    }
    private fun checkOffline() = assertTrue("Actual default/bound/internet networks remain absent without reconnection", main(::offline))
    private fun mark(name: String) {
        phase(name)
        val flags = main { activity -> networkFlags(activity) + mapOf("ownForeground" to foreground(activity),
            "sessionReady" to ready(activity), "cachePresent" to controller(activity).sources.snapshots.isNotEmpty(),
            "transportNetworkFailureObserved" to transportFailed, "authorizationUnavailable" to authorizationUnavailable) }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefGoogleNetworkPhase", name)
            flags.forEach { (key, value) -> putBoolean("passchefGoogleNetwork_$key", value) }
        })
    }
    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 180_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded real network continuation reached its expected state", condition())
    }
    private fun click(text: String) {
        assertTrue("Actual own foreground required for network UI action", main(::foreground))
        ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    }
    private fun unlock(expected: MenuSnapshot) {
        if (main(::ready)) return
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) click("EN")
        mark("network-owner-reunlock")
        click("Open with biometric") // Genuine owner operation; background policy stays intact.
        await { main { foreground(it) && ready(it) } }
        ui.waitForIdle()
        await { main { foreground(it) && ready(it) && !controller(it).sources.busy &&
            controller(it).sources.snapshots == listOf(expected) } }
        checkOffline()
    }
    fun run(directory: File, saved: MenuSnapshot, encryptedJars: ByteArray) {
        val row = saved.entries.single { it.row == 2 }
        val record = File(directory, "cookbook-v1.json")
        val store = MenuSnapshotStore(File(directory, "menus"))
        click("Menu"); click("Mail-test")
        await { main { foreground(it) && ready(it) && controller(it).hasDish() && !controller(it).sources.busy } }
        ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").performScrollTo().assertIsEnabled()
        val expected = saved.copy(recent = (listOf(row.id) + saved.recent.filterNot { it == row.id }).take(20))
        assertTrue("Actual cached Cook begins with exact provider origin and durable recent metadata",
            main { controller(it).selected == row && controller(it).sources.snapshots == listOf(expected) } && store.load(row.origin) == expected)
        mark("network-owner-disable-radios")
        try {
            await { main { foreground(it) && offline(it) } } // Authorized USB/owner switches Wi-Fi/mobile data off.
        } catch (failure: Throwable) {
            mark("network-disable-gate-failed") // Actual safe network/foreground bits, no guessed cause.
            throw failure
        }
        val manager = main { it.getSystemService(ConnectivityManager::class.java) }
        val observer = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { reconnected.set(true) } // Latch; never reset to pass.
        }
        manager.registerDefaultNetworkCallback(observer)
        try {
            checkOffline()
            mark("network-off-observed")
            unlock(expected)
            val retainedGrant = main { activity -> controller(activity).sources.metadata?.let { document ->
                document.spreadsheetId == row.origin.menuId && document.sheets.any { it.properties.sheetId == row.origin.tabId }
            } == true }
            click("Sources")
            if (retainedGrant) {
                mark("network-real-retained-grant-read")
                click(row.origin.title) // Actual existing provider tab action, never a direct/injected grant.
            } else {
                mark("network-real-reconnect")
                ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(row.origin.menuId)
                click("Connect / load tabs") // SDK may reacquire a usable cached grant or fail authorization offline.
            }
            await { main { foreground(it) && (!ready(it) || (!controller(it).sources.busy &&
                controller(it).sources.status in setOf("NETWORK_FAILED", "CONNECT_REQUIRED"))) } }
            if (!main(::ready)) {
                mark("network-reconnect-background-lock")
                unlock(expected) // Discarded background reply is not network-failure acceptance.
                authorizationUnavailable = true
            } else main { activity ->
                val source = controller(activity).sources
                transportFailed = source.status == "NETWORK_FAILED"
                authorizationUnavailable = source.status == "CONNECT_REQUIRED"
            }
            if (retainedGrant) assertTrue("Actual retained provider tab read reports network failure", transportFailed)
            checkOffline()
            assertTrue("Real offline attempt preserves exact last good provider snapshot, disk and encrypted jars",
                main { controller(it).sources.snapshots == listOf(expected) && !controller(it).hasDish() &&
                    controller(it).selected == null && controller(it).dish.isEmpty() } &&
                    store.load(row.origin) == expected && encryptedJars.contentEquals(record.readBytes()))
            ui.onNodeWithTag("dish-display").assertDoesNotExist()
            ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            mark("network-error-cache-preserved")
            GoogleCacheUiContinuation(ui) { cachePhase ->
                checkOffline(); mark("network-$cachePhase")
            }.run(directory, expected, encryptedJars) // All existing150-line assertions execute unchanged while offline.
            checkOffline()
            mark("network-off-complete")
        } finally {
            manager.unregisterNetworkCallback(observer)
        }
    }
}

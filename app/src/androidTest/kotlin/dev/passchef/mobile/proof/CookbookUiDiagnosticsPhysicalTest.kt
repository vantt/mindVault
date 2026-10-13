package dev.passchef.mobile.proof

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Real locked proof Activity only; no setup, authentication or authenticated UI acceptance. */
@RunWith(AndroidJUnit4::class)
class CookbookUiDiagnosticsPhysicalTest {
    private fun lockedFlags(activity: CookbookProofActivity, active: Boolean): Map<String, Boolean> {
        val flags = CookbookUiDiagnostics.stateFlags(activity)
        assertEquals("Locked proof state contains only expected boolean diagnostics", mapOf(
            "vaultUnlocked" to false,
            "vaultBusy" to false,
            "vaultReady" to false,
            "vaultActive" to active,
            "vaultClockExpired" to true,
            "readyDish" to false,
            "lifetimePresent" to false,
            "hasDish" to false,
            "revealed" to false,
            "cooking" to false,
            "dishNonempty" to false
        ), flags)
        return flags
    }

    @Test fun realLockedActivityDiagnosticsSurviveStopAndResumeWithoutAuthentication() {
        val scenario = ActivityScenario.launch(CookbookProofActivity::class.java)
        var directory: File? = null
        var diagnostics: CookbookUiDiagnostics? = null
        try {
            scenario.onActivity { activity ->
                directory = activity.proofDirectory
                diagnostics = CookbookUiDiagnostics(activity.application)
                assertEquals(Lifecycle.State.RESUMED, activity.lifecycle.currentState)
                diagnostics!!.report("locked-initial", activity.lifecycle.currentState.name,
                    lockedFlags(activity, active = true))
            }
            scenario.moveToState(Lifecycle.State.CREATED)
            scenario.onActivity { activity ->
                assertEquals(Lifecycle.State.CREATED, activity.lifecycle.currentState)
                diagnostics!!.report("locked-stopped", activity.lifecycle.currentState.name,
                    lockedFlags(activity, active = false))
            }
            scenario.moveToState(Lifecycle.State.RESUMED)
            scenario.onActivity { activity ->
                assertEquals(Lifecycle.State.RESUMED, activity.lifecycle.currentState)
                diagnostics!!.report("locked-resumed", activity.lifecycle.currentState.name,
                    lockedFlags(activity, active = true))
            }
            assertFalse("No encrypted proof Cookbook was created without setup",
                File(requireNotNull(directory), "cookbook-v1.json").exists())
        } finally {
            try {
                diagnostics?.close()
            } finally {
                try { scenario.close() } finally { directory?.deleteRecursively() }
            }
        }
    }
}

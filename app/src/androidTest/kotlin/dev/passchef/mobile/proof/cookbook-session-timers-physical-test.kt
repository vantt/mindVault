package dev.passchef.mobile.proof

import android.os.SystemClock
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Select each method separately with a ready owner; actual5/10 minutes, no clipboard writes. */
@RunWith(AndroidJUnit4::class)
class CookbookSessionTimersPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()

    @Test fun realFiveMinuteIdleLocksAuthenticatedCookbook() {
        val proof = CookbookTimerFixture(ui)
        proof.run("idle") {
            val baseline = proof.clock()
            assertTrue("Real setup created an original monotonic session", baseline.opened != null)
            proof.arm(baseline)
            proof.report("idle-verify-real-pointer")
            val lastUsed = proof.revealTouch(baseline, baseline.lastUsed)
            val expiry = lastUsed + 300_000
            assertTrue("Idle deadline occurs before original absolute deadline", expiry < requireNotNull(baseline.opened) + 600_000)
            proof.report("idle-baseline-real-pointer")
            for (minute in 1..4) {
                proof.waitUntil(lastUsed + minute * 60_000L, expiry, baseline, lastUsed)
                proof.ready(baseline, lastUsed)
                proof.report("idle-minute-$minute-passive")
            }
            proof.waitUntil(expiry - 5_000, expiry, baseline, lastUsed)
            proof.beforeExpiry(baseline, lastUsed, expiry)
            proof.report("idle-before-original-deadline")
            proof.waitUntil(expiry + 1_500, expiry, baseline, lastUsed)
            proof.expired(baseline, expiry)
            proof.report("idle-complete", true)
        }
    }

    @Test fun realTenMinuteTotalLocksDespiteRepeatedRealTouchesAndCooking() {
        val proof = CookbookTimerFixture(ui)
        proof.run("total") {
            val baseline = proof.clock()
            val opened = requireNotNull(baseline.opened)
            assertTrue("Actual setup/Cook leaves the first minute available", SystemClock.elapsedRealtime() < opened + 60_000)
            proof.arm(baseline)
            proof.report("total-verify-real-pointer")
            var lastUsed = proof.revealTouch(baseline, baseline.lastUsed)
            val expiry = opened + 600_000
            proof.report("total-baseline-real-pointer")
            for (minute in 1..9) {
                proof.waitUntil(opened + minute * 60_000L, expiry, baseline, lastUsed)
                proof.ready(baseline, lastUsed)
                proof.report("total-minute-$minute-cook")
                proof.cook() // Actual Menu/Cook navigation and guarded recooking do not renew original session.
                proof.ready(baseline, lastUsed)
                proof.report("total-minute-$minute-verify-pointer")
                lastUsed = proof.revealTouch(baseline, lastUsed)
                assertTrue("Real periodic pointer remains ahead of idle expiry", lastUsed - opened < 600_000)
                proof.report("total-minute-$minute-real-pointer")
            }
            proof.waitUntil(expiry - 5_000, expiry, baseline, lastUsed)
            proof.beforeExpiry(baseline, lastUsed, expiry)
            assertTrue("Latest real activity prevents idle as original total deadline approaches",
                SystemClock.elapsedRealtime() - lastUsed < 300_000)
            proof.report("total-before-original-deadline")
            proof.waitUntil(expiry + 1_500, expiry, baseline, lastUsed)
            proof.expired(baseline, expiry)
            proof.report("total-complete", true)
        }
    }
}

package dev.passchef.mobile.proof

import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Disposable API26–27 emulator adapter acceptance only; no Cookbook authentication or cooking. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 26, maxSdkVersion = 27)
class Api26ClipboardFallbackTest {
    private fun proof(action: (Api26ClipboardFixture) -> Unit) {
        val fixture = Api26ClipboardFixture()
        var primary: Throwable? = null
        try { fixture.open(); action(fixture) }
        catch (failure: Throwable) { primary = failure; throw failure }
        finally { fixture.close(primary) }
    }

    @Test fun untouchedDishExpiresIntoActualEmptyClip() = proof { fixture ->
        val copied = fixture.copyDish("untouched")
        fixture.until(copied.deadline - 1_500)
        assertTrue("Observed owned Dish still precedes its original deadline", SystemClock.elapsedRealtime() < copied.deadline)
        fixture.matches(copied.label)
        assertTrue("Original deadline and generation remain unchanged", fixture.claim() == copied)
        fixture.until(copied.deadline + 500)
        fixture.emptyFallback(); fixture.forgotten(copied.generation)
        fixture.report("api26-untouched-fallback-passed", copied)
    }

    @Test fun foreignAndNullLabelCopiesSurviveOriginalExpiry() = proof { fixture ->
        for (nullLabel in listOf(false, true)) {
            val copied = fixture.copyDish("foreign-$nullLabel")
            val replacement = fixture.replace(nullLabel)
            assertTrue("Foreign write does not mutate original owned policy", fixture.claim() == copied)
            fixture.until(copied.deadline + 500)
            fixture.matches(replacement); fixture.forgotten(copied.generation)
            fixture.report(if (nullLabel) "api26-null-label-preserved" else "api26-foreign-preserved", copied)
        }
    }

    @Test fun newerDishRetainsItsOwnLaterDeadline() = proof { fixture ->
        val first = fixture.copyDish("older")
        fixture.until(first.deadline - 25_000)
        val newer = fixture.copyDish("newer")
        assertTrue("New Copy advances generation and retains a distinct later deadline",
            newer.generation == first.generation + 1 && newer.deadline > first.deadline)
        fixture.until(first.deadline + 500)
        assertTrue("Older deadline observation still precedes the newer deadline", SystemClock.elapsedRealtime() < newer.deadline)
        fixture.matches(newer.label)
        assertTrue("Older work cannot change newer ownership", fixture.claim() == newer)
        fixture.until(newer.deadline + 500)
        fixture.emptyFallback(); fixture.forgotten(newer.generation)
        fixture.report("api26-newer-deadline-passed", newer)
    }

    @Test fun usernameCopySurvivesRetiredDishCleanup() = proof { fixture ->
        val copied = fixture.copyDish("before-username")
        fixture.username(); fixture.forgotten(copied.generation)
        fixture.until(copied.deadline + 500)
        fixture.matches("PassChef username"); fixture.forgotten(copied.generation)
        fixture.report("api26-username-preserved", copied)
    }
}

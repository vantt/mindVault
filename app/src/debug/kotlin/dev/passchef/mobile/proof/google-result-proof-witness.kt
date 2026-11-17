package dev.passchef.mobile.proof

import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.cookbook.AndroidSheetsAuthorization
import dev.passchef.mobile.cookbook.CookbookController

data class GoogleResultProofSnapshot(val launches: Int, val results: Int, val forwarded: Int,
    val requestCode: Int?, val resultCode: Int?, val matchedOriginal: Boolean,
    val originalStillPending: Boolean, val consumed: Boolean, val failed: Boolean)

/** Read-only debug witness. All methods run on main; no reply Intent/token contents are inspected. */
class GoogleResultProofWitness(private val activity: ColdStartProofActivity) {
    private var original: Any? = null
    private var code: Int? = null
    private var result: Int? = null
    private var launches = 0
    private var results = 0
    private var forwarded = 0
    private var matchedOriginal = false
    private var failed = false
    private fun pending(): Any? {
        val book = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }
            .get(activity) as CookbookController
        return AndroidSheetsAuthorization::class.java.getDeclaredField("pending").apply { isAccessible = true }
            .get(book.authorization)
    }
    private fun observe(action: () -> Unit) {
        try { action() } catch (_: Throwable) { failed = true }
    }
    fun launched(requestCode: Int) = observe {
        launches++
        val request = pending()
        if (launches != 1 || request == null || requestCode < 0) failed = true
        else { original = request; code = requestCode }
    }
    fun beforeResult(requestCode: Int, resultCode: Int) = observe {
        if (requestCode != code) return@observe
        results++
        if (results != 1 || original == null || pending() !== original) failed = true
        else { result = resultCode; matchedOriginal = true }
    }
    fun afterResult(requestCode: Int, returnedNormally: Boolean) = observe {
        if (requestCode != code) return@observe
        forwarded++
        if (!returnedNormally || forwarded != 1 || !matchedOriginal) failed = true
        val remaining = pending()
        if (remaining != null && remaining !== original) failed = true
        // The normal registry may deliver its callback later at ON_START.
    }
    fun snapshot(): GoogleResultProofSnapshot {
        var same = false
        var consumed = false
        observe {
            val remaining = pending()
            same = original != null && remaining === original
            consumed = original != null && remaining == null
            if (remaining != null && original != null && remaining !== original) failed = true
        }
        return GoogleResultProofSnapshot(launches, results, forwarded, code, result, matchedOriginal, same, consumed, failed)
    }
}

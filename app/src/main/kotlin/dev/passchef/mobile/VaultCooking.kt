package dev.passchef.mobile

import android.os.Handler
import dev.passchef.mobile.recipe.*
import java.util.concurrent.Executor

/** Jar copies originate on the guarded main thread, never from a shared worker session. */
class VaultCooking(private val worker: Executor, private val main: Handler, private val ready: () -> Boolean) {
    @Volatile private var generation = 0L
    fun invalidate() { generation++ }
    fun cook(text: String, menuId: String?, jar: ByteArray?, callback: (CookingResult?, String?) -> Unit) {
        val token = ++generation
        if (!ready() || jar == null) { jar?.fill(0); callback(null, "MISSING_SPICE"); return }
        try {
            worker.execute {
                try {
                    if (token != generation) return@execute
                    val spice = jar.decodeToString(throwOnInvalidSequence = true)
                    val kitchen = GeneratePassword(SeasoningProvider { _, menu -> Seasoning(spice, menuId = menu) }, TagCodec())
                    val result = kitchen.execute(text, menuId)
                    main.post { if (token == generation && ready()) callback(result, null) }
                } catch (error: Exception) {
                    val code = if (error is RecipeProfileMismatchException) "RECIPE_MISMATCH" else "INVALID_RECIPE"
                    main.post { if (token == generation && ready()) callback(null, code) }
                } finally { jar.fill(0) }
            }
        } catch (_: Exception) { jar.fill(0); if (ready()) callback(null, "LOCKED") }
    }
}

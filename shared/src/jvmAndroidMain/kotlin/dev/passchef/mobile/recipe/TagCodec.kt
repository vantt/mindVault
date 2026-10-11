package dev.passchef.mobile.recipe

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class TagCodec : RecipeTagVerifier {
    fun compute(recipe: Recipe, rawSpice: String, menuId: String): String {
        require(rawSpice.isNotEmpty() && menuId.isNotEmpty()) { "Seasoning and Menu context are required" }
        val keyBytes = webUnicode(rawSpice).encodeToByteArray()
        return try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(keyBytes, "HmacSHA256"))
            base32(mac.doFinal(webUnicode(recipe.canonical() + "|" + menuId).encodeToByteArray())).take(4)
        } finally { keyBytes.fill(0) }
    }

    override fun verify(recipe: Recipe, rawSpice: String, menuId: String): Boolean =
        recipe.tag != null && compute(recipe, rawSpice, menuId) == recipe.tag
}

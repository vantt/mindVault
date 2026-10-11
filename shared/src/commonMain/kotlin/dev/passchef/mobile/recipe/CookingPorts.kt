package dev.passchef.mobile.recipe

data class Seasoning(
    val raw: String,
    val effective: String = raw,
    val profileName: String = "Default",
    val isShared: Boolean = false,
    val settings: Map<String, String> = emptyMap(),
    val menuId: String? = null
)

fun interface SeasoningProvider {
    fun forCooking(index: Int, menuId: String?): Seasoning?
}

fun interface RecipeTagVerifier {
    fun verify(recipe: Recipe, rawSpice: String, menuId: String): Boolean
}

class MapSeasoningProvider(private val spices: Map<Int, String>) : SeasoningProvider {
    override fun forCooking(index: Int, menuId: String?): Seasoning? =
        spices[index]?.takeIf { it.isNotEmpty() }?.let { Seasoning(it, menuId = menuId?.ifEmpty { null }) }
}

data class CookingResult(
    val password: String,
    val profileName: String,
    val isShared: Boolean,
    val settings: Map<String, String>,
    val warning: String? = null
)

class RecipeProfileMismatchException : IllegalArgumentException("Recipe does not match its profile and Menu") {
    val code = "RECIPE_MISMATCH"
}

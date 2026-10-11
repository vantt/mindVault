package dev.passchef.mobile.recipe

/** Pure cooking logic; storage and tag verification are supplied by the caller. */
class GeneratePassword(
    private val provider: SeasoningProvider,
    private val verifier: RecipeTagVerifier
) {
    fun execute(text: String?, menuId: String? = null): CookingResult {
        val recipe = RecipeParser.parse(text) ?: throw IllegalArgumentException("Invalid recipe format")
        val seasoning = provider.forCooking(recipe.spiceIndex, menuId)
            ?: throw IllegalArgumentException("Secret #${recipe.spiceIndex} not found")
        if (recipe.tag != null && (seasoning.menuId.isNullOrEmpty() ||
                !verifier.verify(recipe, seasoning.raw, seasoning.menuId))) {
            throw RecipeProfileMismatchException()
        }
        var spice = seasoning.effective
        if ('~' in recipe.modifiers) spice = spice.replace(Regex("[^a-zA-Z0-9]"), "")
        if ('?' in recipe.modifiers) spice = spice.toCharArray().reversed().joinToString("")
        if ('!' in recipe.modifiers) spice = jsUppercase(spice)
        var position = recipe.position
        if ('_' in recipe.modifiers) position = when (position) { '#' -> '$'; '$' -> '#'; else -> position }
        val password = combine(recipe.ingredient, position, spice)
        return CookingResult(password, seasoning.profileName, seasoning.isShared, seasoning.settings,
            if (recipe.tag == null && !seasoning.menuId.isNullOrEmpty()) "legacy_no_tag" else null)
    }

    private fun combine(ingredient: String, position: Char, spice: String): String = when (position) {
        '#' -> spice + ingredient
        '$' -> ingredient + spice
        '@' -> ingredient.substring(0, ingredient.length / 2) + spice + ingredient.substring(ingredient.length / 2)
        '%' -> interleave(ingredient, spice, 1)
        '^' -> interleave(ingredient, spice, 2)
        else -> throw IllegalArgumentException("Unknown cooking position")
    }

    private fun interleave(first: String, second: String, size: Int): String = buildString {
        var i = 0
        var j = 0
        while (i < first.length || j < second.length) {
            if (i < first.length) { append(first.substring(i, minOf(i + size, first.length))); i += size }
            if (j < second.length) { append(second.substring(j, minOf(j + size, second.length))); j += size }
        }
    }
}

package dev.passchef.mobile.recipe

const val TAG_ALPHABET = "abcdefghijkmnpqrstuvwxyz23456789"

data class Recipe(
    val ingredient: String,
    val position: Char,
    val spiceIndex: Int,
    val modifiers: List<Char> = emptyList(),
    val tag: String? = null
) {
    fun canonical(): String = "$ingredient$position$spiceIndex${modifiers.distinct().sorted().joinToString("")}"
}

object RecipeParser {
    private val grammar = Regex("^([a-zA-Z0-9]+)([#@$%^])([0-9])([_!?~]*)(?:\\.([a-z2-9]{4}))?$")

    fun parse(text: String?): Recipe? {
        val match = text?.let { grammar.matchEntire(it) } ?: return null
        val index = match.groupValues[3].toInt()
        val tag = match.groupValues[5].ifEmpty { null }
        if (index !in 1..5 || tag?.any { it !in TAG_ALPHABET } == true) return null
        return Recipe(match.groupValues[1], match.groupValues[2].single(), index,
            match.groupValues[4].toList().distinct(), tag)
    }
}

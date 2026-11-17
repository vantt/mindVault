package dev.passchef.mobile.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.ui.PassChefMonoFamily
import dev.passchef.mobile.ui.PassChefPalette

/**
 * Plain gloss for a written code (spec v2: always visible, never raw syntax alone), matching the
 * extension's generate_password.js: # front, $ end, @ middle, % / ^ mixed in; _ swaps # and $;
 * ! upper case, ? reversed, ~ symbols removed. Optional set name for D17.
 */
fun writtenCodeGloss(code: String, vi: Boolean, setName: String? = null): String? {
    val recipe = RecipeParser.parse(code) ?: return null
    var position = recipe.position
    if ('_' in recipe.modifiers) position = when (position) { '#' -> '$'; '$' -> '#'; else -> position }
    val n = recipe.spiceIndex
    val base = when (position) {
        '#' -> if (vi) "thêm phần bí mật số $n vào đầu" else "add secret part $n in front"
        '$' -> if (vi) "thêm phần bí mật số $n vào cuối" else "add secret part $n at the end"
        '@' -> if (vi) "chèn phần bí mật số $n vào giữa" else "put secret part $n in the middle"
        '%' -> if (vi) "xen kẽ từng ký tự với phần bí mật số $n" else "mix in secret part $n, 1 character at a time"
        else -> if (vi) "xen kẽ từng 2 ký tự với phần bí mật số $n" else "mix in secret part $n, 2 characters at a time"
    }
    val extras = buildList {
        if ('~' in recipe.modifiers) add(if (vi) "bỏ ký hiệu khỏi phần bí mật" else "symbols removed from the secret part")
        if ('?' in recipe.modifiers) add(if (vi) "đảo ngược phần bí mật" else "secret part reversed")
        if ('!' in recipe.modifiers) add(if (vi) "phần bí mật viết HOA" else "secret part in CAPITALS")
    }
    val set = setName?.let { if (vi) " · bộ $it" else " · $it set" }.orEmpty()
    return "= " + (listOf(base) + extras).joinToString(", ") + set
}

/** Code in mono (random part + marker in accent-ink) with its gloss underneath. */
@Composable fun WrittenCode(code: String, setName: String? = null, modifier: Modifier = Modifier) {
    val vi = LocalCookbookVietnamese.current
    val recipe = RecipeParser.parse(code)
    Column(modifier) {
        Text(buildAnnotatedString {
            if (recipe == null) append(code) else {
                append(recipe.ingredient)
                withStyle(SpanStyle(color = PassChefPalette.AccentInk)) { append(code.removePrefix(recipe.ingredient)) }
            }
        }, style = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily))
        writtenCodeGloss(code, vi, setName)?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        }
    }
}

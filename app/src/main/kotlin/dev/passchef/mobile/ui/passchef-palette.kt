package dev.passchef.mobile.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Palette D (D13): Ink+Amber dark and Linen light, both first-class. Amber fills only the one
 * main action per screen (+ logo). Text/icon accents use AccentInk (= amber on dark, #8a5610 on light).
 * Hairlines are structural, never the only edge of a control.
 */
data class PassChefTokens(
    val dark: Boolean,
    val canvas: Color, val surface: Color, val raised: Color, val hairline: Color, val strongLine: Color,
    val text: Color, val body: Color, val secondary: Color, val muted: Color, val outline: Color,
    val amber: Color, val onAmber: Color, val accentInk: Color, val amberBackground: Color,
    val moss: Color, val mossBackground: Color, val coral: Color, val coralBackground: Color, val honey: Color
)

val InkAmberTokens = PassChefTokens(true,
    canvas = Color(0xFF0E0E10), surface = Color(0xFF15151A), raised = Color(0xFF1C1C22),
    hairline = Color(0xFF232329), strongLine = Color(0xFF2F2F36),
    text = Color(0xFFF0ECE2), body = Color(0xFFD8D2C3), secondary = Color(0xFFB4AD9F), muted = Color(0xFF8A857A),
    outline = Color(0xFF6A655D), amber = Color(0xFFE8A341), onAmber = Color(0xFF0E0E10),
    accentInk = Color(0xFFE8A341), amberBackground = Color(0xFF3A2A14),
    moss = Color(0xFF84B577), mossBackground = Color(0xFF1D2818),
    coral = Color(0xFFE0746C), coralBackground = Color(0xFF2B1815), honey = Color(0xFFD4A548))

val LinenTokens = PassChefTokens(false,
    canvas = Color(0xFFF8F5EF), surface = Color(0xFFFFFDF9), raised = Color(0xFFFFFFFF),
    hairline = Color(0xFFE3DCCF), strongLine = Color(0xFFD3CABA),
    text = Color(0xFF1D1B17), body = Color(0xFF2E2B25), secondary = Color(0xFF4C4840), muted = Color(0xFF68645B),
    outline = Color(0xFF8A8478), amber = Color(0xFFE8A341), onAmber = Color(0xFF1D1B17),
    accentInk = Color(0xFF8A5610), amberBackground = Color(0xFFFBEDD2),
    moss = Color(0xFF3C7535), mossBackground = Color(0xFFE6EFE2),
    coral = Color(0xFFB4372D), coralBackground = Color(0xFFF8E3E0), honey = Color(0xFF8A6100))

val LocalPassChefTokens = staticCompositionLocalOf { InkAmberTokens }

/** Theme-aware roles; read inside composition only. Names kept from the dark-only Precision palette. */
object PassChefPalette {
    private val t: PassChefTokens @Composable @ReadOnlyComposable get() = LocalPassChefTokens.current
    val Dark: Boolean @Composable @ReadOnlyComposable get() = t.dark
    val Canvas: Color @Composable @ReadOnlyComposable get() = t.canvas
    val Surface: Color @Composable @ReadOnlyComposable get() = t.surface
    val Raised: Color @Composable @ReadOnlyComposable get() = t.raised
    val Hairline: Color @Composable @ReadOnlyComposable get() = t.hairline
    val StrongLine: Color @Composable @ReadOnlyComposable get() = t.strongLine
    val Text: Color @Composable @ReadOnlyComposable get() = t.text
    val Body: Color @Composable @ReadOnlyComposable get() = t.body
    val Secondary: Color @Composable @ReadOnlyComposable get() = t.secondary
    val Muted: Color @Composable @ReadOnlyComposable get() = t.muted
    val Outline: Color @Composable @ReadOnlyComposable get() = t.outline
    val Amber: Color @Composable @ReadOnlyComposable get() = t.amber
    val OnAmber: Color @Composable @ReadOnlyComposable get() = t.onAmber
    val AccentInk: Color @Composable @ReadOnlyComposable get() = t.accentInk
    val AmberBackground: Color @Composable @ReadOnlyComposable get() = t.amberBackground
    val Moss: Color @Composable @ReadOnlyComposable get() = t.moss
    val MossBackground: Color @Composable @ReadOnlyComposable get() = t.mossBackground
    val Coral: Color @Composable @ReadOnlyComposable get() = t.coral
    val CoralBackground: Color @Composable @ReadOnlyComposable get() = t.coralBackground
    val Honey: Color @Composable @ReadOnlyComposable get() = t.honey
}

fun passChefColorScheme(t: PassChefTokens): ColorScheme {
    val base = if (t.dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = t.amber, onPrimary = t.onAmber, primaryContainer = t.amberBackground, onPrimaryContainer = t.accentInk,
        inversePrimary = t.amberBackground, secondary = t.secondary, onSecondary = t.canvas,
        secondaryContainer = t.raised, onSecondaryContainer = t.secondary,
        tertiary = t.moss, onTertiary = t.canvas, tertiaryContainer = t.mossBackground, onTertiaryContainer = t.moss,
        background = t.canvas, onBackground = t.text, surface = t.surface, onSurface = t.text,
        surfaceVariant = t.raised, onSurfaceVariant = t.secondary, surfaceTint = t.amber,
        inverseSurface = t.text, inverseOnSurface = t.canvas, error = t.coral, onError = t.canvas,
        errorContainer = t.coralBackground, onErrorContainer = t.coral, outline = t.outline, outlineVariant = t.hairline,
        scrim = Color(0xFF0E0E10), surfaceBright = t.raised, surfaceDim = t.canvas, surfaceContainer = t.surface,
        surfaceContainerHigh = t.raised, surfaceContainerHighest = t.raised, surfaceContainerLow = t.surface,
        surfaceContainerLowest = t.canvas)
}

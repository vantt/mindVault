package dev.passchef.mobile.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.passchef.mobile.R

/**
 * Static instances cut from the variable fonts (fontTools instancer): runtime variation settings were
 * ignored on device and Fraunces fell back to its wght 900 / opsz 9 / WONK 1 default. Matches the design:
 * Fraunces wght 400, opsz 32, SOFT 0, WONK 0; Geist 400/500/600; Geist Mono 400/500.
 */
val PassChefDisplayFamily = FontFamily(
    Font(R.font.fraunces_display_regular, FontWeight.Normal),
    Font(R.font.fraunces_display_italic, FontWeight.Normal, FontStyle.Italic)
)
val PassChefBodyFamily = FontFamily(
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_semibold, FontWeight.SemiBold)
)
val PassChefMonoFamily = FontFamily(
    Font(R.font.geist_mono_regular, FontWeight.Normal),
    Font(R.font.geist_mono_medium, FontWeight.Medium)
)

/** Only static ASCII/NFC titles use this style; dynamic/freeform labels use Geist. */
val PassChefDisplay = TextStyle(fontFamily = PassChefDisplayFamily,
    fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 34.sp)
val PassChefDish = TextStyle(fontFamily = PassChefMonoFamily,
    fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 30.sp)

private fun body(size: Int, line: Int, weight: FontWeight = FontWeight.Normal) =
    TextStyle(fontFamily = PassChefBodyFamily, fontWeight = weight,
        fontSize = size.sp, lineHeight = line.sp)

/** All Material defaults are Geist, including headlines that may contain user labels. */
val PassChefTypography = Typography(
    displayLarge = body(48, 56),
    displayMedium = body(40, 48),
    displaySmall = body(32, 40),
    headlineLarge = body(32, 40),
    headlineMedium = body(28, 36),
    headlineSmall = body(24, 32),
    titleLarge = body(22, 28, FontWeight.Medium),
    titleMedium = body(16, 24, FontWeight.Medium),
    titleSmall = body(14, 20, FontWeight.Medium),
    bodyLarge = body(16, 24),
    bodyMedium = body(14, 20),
    bodySmall = body(12, 18),
    labelLarge = body(14, 20, FontWeight.Medium),
    labelMedium = body(12, 16, FontWeight.Medium),
    labelSmall = body(12, 16, FontWeight.Medium)
)

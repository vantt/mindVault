package dev.passchef.mobile.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

/** Settings › Theme (D13). SYSTEM follows the phone. */
enum class ThemeChoice { SYSTEM, DARK, LIGHT }

@Composable
fun PassChefTheme(choice: ThemeChoice = ThemeChoice.SYSTEM, content: @Composable () -> Unit) {
    val dark = when (choice) { ThemeChoice.SYSTEM -> isSystemInDarkTheme(); ThemeChoice.DARK -> true; ThemeChoice.LIGHT -> false }
    val tokens = if (dark) InkAmberTokens else LinenTokens
    SystemBars(tokens)
    CompositionLocalProvider(LocalPassChefTokens provides tokens) {
        MaterialTheme(
            colorScheme = passChefColorScheme(tokens),
            typography = PassChefTypography,
            shapes = Shapes(
                extraSmall = RoundedCornerShape(4.dp),
                small = RoundedCornerShape(4.dp),
                medium = RoundedCornerShape(6.dp),
                large = RoundedCornerShape(12.dp),
                extraLarge = RoundedCornerShape(16.dp)
            ),
            content = content
        )
    }
}

/** Status/navigation bar colour + icon contrast follow the in-app theme (D13). */
@Suppress("DEPRECATION")
@Composable private fun SystemBars(tokens: PassChefTokens) {
    val view = androidx.compose.ui.platform.LocalView.current
    if (view.isInEditMode) return
    androidx.compose.runtime.SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        val color = tokens.canvas.toArgb()
        window.statusBarColor = color; window.navigationBarColor = color
        window.decorView.setBackgroundColor(color)
        androidx.core.view.WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !tokens.dark; isAppearanceLightNavigationBars = !tokens.dark
        }
    }
}

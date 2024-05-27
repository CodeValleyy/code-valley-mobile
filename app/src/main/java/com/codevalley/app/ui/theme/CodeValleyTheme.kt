package com.codevalley.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF902DE0),
    secondary = Color(0xFFE07E2D),
    background = Color(0xFF2C2C2C),
    surface = Color(0xFF424242)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF902DE0),
    secondary = Color(0xFFE07E2D),
    background = Color(0xFFEDEDED),
    surface = Color(0xFFFFFFFF)
)

private val AppTypography = Typography()
private val AppShapes = Shapes()

@Composable
fun CodeValleyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
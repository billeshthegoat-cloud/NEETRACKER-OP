package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LavenderDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B2D54),
    onPrimaryContainer = LavenderPrimary,
    secondary = BlushPinkDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A2536),
    onSecondaryContainer = BlushPink,
    tertiary = WarmPeachDark,
    onTertiary = DeepBlack,
    background = DarkBg,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = CardGlassBorderDark,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LavenderDeep,
    onPrimary = Color.White,
    primaryContainer = LavenderSoftBg,
    onPrimaryContainer = DeepBlack,
    secondary = BlushPinkDark,
    onSecondary = Color.White,
    secondaryContainer = BlushPink,
    onSecondaryContainer = DeepBlack,
    tertiary = WarmPeachDark,
    onTertiary = DeepBlack,
    background = OffWhiteBg,
    onBackground = TextPrimaryLight,
    surface = CardGlassLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightLavenderTint,
    onSurfaceVariant = TextSecondaryLight,
    outline = CardGlassBorderLight,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun NeetrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

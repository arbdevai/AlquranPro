package com.arbani.alquranpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * iOS 26 Flat System Gray Theme - No gradients, fully native grouped aesthetic.
 */

// iOS System Grouped Backgrounds (Flat Gray)
val IosLightGroupedBg = Color(0xFFF2F2F7)
val IosLightCard = Color(0xFFFFFFFF)
val IosLightSecondary = Color(0xFFE5E5EA)
val IosLightBorder = Color(0xFFD1D1D6)

val IosDarkGroupedBg = Color(0xFF000000)
val IosDarkCard = Color(0xFF1C1C1E)
val IosDarkSecondary = Color(0xFF2C2C2E)
val IosDarkBorder = Color(0xFF38383A)

val IosTextPrimaryLight = Color(0xFF1C1C1E)
val IosTextSecondaryLight = Color(0xFF636366)
val IosTextPrimaryDark = Color(0xFFF2F2F7)
val IosTextSecondaryDark = Color(0xFFAEAEB2)

// Neutral Graphite Accent (replaces Emerald/Gold)
val GraphitePrimary = Color(0xFF374151)
val GraphitePrimaryDark = Color(0xFFD1D5DB)
val GraphiteContainer = Color(0xFFE5E7EB)
val GraphiteContainerDark = Color(0xFF3A3A3C)

private val DarkColorScheme = darkColorScheme(
    primary = GraphitePrimaryDark,
    onPrimary = Color(0xFF111827),
    primaryContainer = GraphiteContainerDark,
    onPrimaryContainer = IosTextPrimaryDark,
    secondary = GraphitePrimaryDark,
    onSecondary = Color(0xFF111827),
    background = IosDarkGroupedBg,
    onBackground = IosTextPrimaryDark,
    surface = IosDarkCard,
    onSurface = IosTextPrimaryDark,
    surfaceVariant = IosDarkSecondary,
    onSurfaceVariant = IosTextSecondaryDark,
    surfaceContainer = IosDarkSecondary,
    surfaceContainerLow = IosDarkCard,
    outline = Color(0xFF8E8E93),
    outlineVariant = IosDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = GraphitePrimary,
    onPrimary = Color.White,
    primaryContainer = GraphiteContainer,
    onPrimaryContainer = Color(0xFF1F2937),
    secondary = GraphitePrimary,
    onSecondary = Color.White,
    background = IosLightGroupedBg,
    onBackground = IosTextPrimaryLight,
    surface = IosLightCard,
    onSurface = IosTextPrimaryLight,
    surfaceVariant = IosLightSecondary,
    onSurfaceVariant = IosTextSecondaryLight,
    surfaceContainer = IosLightSecondary,
    surfaceContainerLow = IosLightCard,
    outline = Color(0xFF8E8E93),
    outlineVariant = IosLightBorder
)

@Composable
fun AlquranProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

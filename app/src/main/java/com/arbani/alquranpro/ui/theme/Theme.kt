package com.arbani.alquranpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Modern iOS Grouped Theme with elegant jewel accents:
 * - Neutral iOS grouped background
 * - Tasteful Emerald/Teal primary accent for Quran reading
 * - Warm Amber accent for Tasbih/Tahlil
 * - Indigo accent for Prayer schedules
 */

// iOS Grouped Neutral Surfaces
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

// Fresh Modern iOS Jewel Accents
val AccentTealLight = Color(0xFF0F766E)
val AccentTealContainerLight = Color(0xFFE6F4F1)
val AccentTealDark = Color(0xFF2DD4BF)
val AccentTealContainerDark = Color(0xFF134E48)

val AccentAmberLight = Color(0xFFB45309)
val AccentAmberContainerLight = Color(0xFFFEF3C7)
val AccentAmberDark = Color(0xFFFBBF24)
val AccentAmberContainerDark = Color(0xFF78350F)

val AccentIndigoLight = Color(0xFF4338CA)
val AccentIndigoContainerLight = Color(0xFFEEF2FF)
val AccentIndigoDark = Color(0xFF818CF8)
val AccentIndigoContainerDark = Color(0xFF312E81)

private val DarkColorScheme = darkColorScheme(
    primary = AccentTealDark,
    onPrimary = Color(0xFF042F2E),
    primaryContainer = AccentTealContainerDark,
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondary = AccentAmberDark,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = AccentAmberContainerDark,
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = AccentIndigoDark,
    onTertiary = Color(0xFF1E1B4B),
    tertiaryContainer = AccentIndigoContainerDark,
    onTertiaryContainer = Color(0xFFEEF2FF),
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
    primary = AccentTealLight,
    onPrimary = Color.White,
    primaryContainer = AccentTealContainerLight,
    onPrimaryContainer = Color(0xFF115E59),
    secondary = AccentAmberLight,
    onSecondary = Color.White,
    secondaryContainer = AccentAmberContainerLight,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = AccentIndigoLight,
    onTertiary = Color.White,
    tertiaryContainer = AccentIndigoContainerLight,
    onTertiaryContainer = Color(0xFF312E81),
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

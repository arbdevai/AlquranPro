package com.arbani.alquranpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val EmeraldPrimary = Color(0xFF0D9488)       // iOS Teal-Emerald
val EmeraldDark = Color(0xFF0F766E)
val EmeraldLight = Color(0xFF14B8A6)
val SageGold = Color(0xFFF59E0B)             // Warm gold accent
val SageGoldLight = Color(0xFFFCD34D)

// iOS Glass Backgrounds
val GlassLightBg = Color(0xFFF8FAFC)
val GlassLightCard = Color(0xFFFFFFFF)
val GlassLightCardBorder = Color(0x1A0F172A)
val GlassLightSecondary = Color(0xFFF1F5F9)

val GlassDarkBg = Color(0xFF0B131E)
val GlassDarkCard = Color(0xFF131F2E)
val GlassDarkCardBorder = Color(0x2E94A3B8)
val GlassDarkSecondary = Color(0xFF1E293B)

val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color(0xFF042F2C),
    primaryContainer = EmeraldDark,
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondary = SageGoldLight,
    onSecondary = Color(0xFF451A03),
    background = GlassDarkBg,
    onBackground = TextPrimaryDark,
    surface = GlassDarkCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = GlassDarkSecondary,
    onSurfaceVariant = TextSecondaryDark,
    outline = GlassDarkCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCFBF1),
    onPrimaryContainer = Color(0xFF115E59),
    secondary = SageGold,
    onSecondary = Color.White,
    background = GlassLightBg,
    onBackground = TextPrimaryLight,
    surface = GlassLightCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = GlassLightSecondary,
    onSurfaceVariant = TextSecondaryLight,
    outline = GlassLightCardBorder
)

object IOSGradients {
    val HeroEmerald = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0D9488),
            Color(0xFF0F766E),
            Color(0xFF115E59)
        )
    )

    val HeroPrayerNight = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1E1B4B),
            Color(0xFF312E81),
            Color(0xFF0F766E)
        )
    )

    val GlassCardGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0x14FFFFFF),
            Color(0x05FFFFFF)
        )
    )

    val GoldPill = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFF59E0B),
            Color(0xFFFBBF24)
        )
    )
}

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

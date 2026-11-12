package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color(0xFF00290E),
    primaryContainer = NeonGreenContainer,
    onPrimaryContainer = NeonGreen,

    secondary = NeonCyan,
    onSecondary = Color(0xFF00272B),
    secondaryContainer = NeonCyanContainer,
    onSecondaryContainer = NeonCyan,

    tertiary = NeonPurple,
    onTertiary = Color(0xFF280345),
    tertiaryContainer = NeonPurpleContainer,
    onTertiaryContainer = NeonPurple,

    background = CyberBackground,
    onBackground = TextPrimary,

    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    outline = CyberBorder,
    outlineVariant = CyberBorderSubtle,

    error = NeonRed,
    onError = Color.White,
    errorContainer = NeonRedContainer,
    onErrorContainer = NeonRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}

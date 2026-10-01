package com.example.pokeapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PokedexColorScheme = lightColorScheme(
    primary = PokedexLimeStrong,
    onPrimary = PokedexText,
    primaryContainer = PokedexLime,
    onPrimaryContainer = PokedexText,
    secondary = PokedexYellow,
    onSecondary = PokedexText,
    secondaryContainer = PokedexYellow,
    onSecondaryContainer = PokedexText,
    background = PokedexBackground,
    onBackground = PokedexText,
    surface = PokedexSurface,
    onSurface = PokedexText,
    surfaceVariant = PokedexSoft,
    onSurfaceVariant = PokedexSecondaryText,
    outlineVariant = PokedexBorder
)

@Composable
fun PokeApplicationTheme(content: @Composable () -> Unit) {
    // Keep Home's light Figma palette independent of the device wallpaper.
    MaterialTheme(
        colorScheme = PokedexColorScheme,
        typography = Typography,
        content = content
    )
}

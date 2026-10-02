package com.example.pokeapplication.ui.theme

import androidx.compose.ui.graphics.Color
import java.util.Locale

// Pastel backgrounds belong to Presentation; the domain model keeps only the type name.
private val typeBackgrounds = mapOf(
    "normal" to Color(0xFFEAE7DF),
    "fire" to Color(0xFFFFDFCA),
    "water" to Color(0xFFD8EAFB),
    "electric" to PokedexYellow,
    "grass" to PokedexLime,
    "ice" to Color(0xFFD8F2F0),
    "fighting" to Color(0xFFF0D5CE),
    "poison" to Color(0xFFEAD9F1),
    "ground" to Color(0xFFF0E2C6),
    "flying" to Color(0xFFE3E5FA),
    "psychic" to Color(0xFFFADBE7),
    "bug" to Color(0xFFE5EDC8),
    "rock" to Color(0xFFE6DDCC),
    "ghost" to Color(0xFFDDD9EE),
    "dragon" to Color(0xFFDBD6F6),
    "dark" to Color(0xFFDCD7D4),
    "steel" to Color(0xFFDDE6EA),
    "fairy" to Color(0xFFF5DEEF)
)

fun pokemonTypeBackground(typeName: String?): Color =
    typeBackgrounds[typeName?.trim()?.lowercase(Locale.ROOT)] ?: PokedexSoft

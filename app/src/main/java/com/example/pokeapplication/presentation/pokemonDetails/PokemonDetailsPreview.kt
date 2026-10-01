package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokeapplication.domain.model.Ability
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonStat
import com.example.pokeapplication.domain.model.PokemonType
import com.example.pokeapplication.ui.theme.PokeApplicationTheme

private val previewPokemon = Pokemon(
    id = 1, name = "bulbasaur", imageUrl = "", height = 7, weight = 69, baseExperience = 64,
    types = listOf(PokemonType("grass"), PokemonType("poison")),
    abilities = listOf(Ability("overgrow", false), Ability("chlorophyll", true)),
    stats = listOf(PokemonStat("hp", 45), PokemonStat("attack", 49), PokemonStat("defense", 49),
        PokemonStat("special-attack", 65), PokemonStat("special-defense", 65), PokemonStat("speed", 45))
)

@Preview(name = "Details", widthDp = 390, heightDp = 844)
@Preview(name = "Compact details", widthDp = 320, heightDp = 640)
@Preview(name = "Large text details", widthDp = 390, heightDp = 844, fontScale = 1.3f)
@Composable
fun PokemonDetailsPreview() {
    var pokemon by remember { mutableStateOf(previewPokemon) }
    PokeApplicationTheme {
        PokemonDetailsContent(PokemonDetailsUiState(pokemon = pokemon, isLoading = false),
            onBack = {}, onRetry = {}, onFavoriteClick = { pokemon = pokemon.copy(isFavorite = !pokemon.isFavorite) })
    }
}

@Preview(name = "Loading details", widthDp = 390, heightDp = 844)
@Composable
private fun PokemonDetailsLoadingPreview() {
    PokeApplicationTheme { PokemonDetailsContent(PokemonDetailsUiState(), {}, {}, {}) }
}

@Preview(name = "Missing Pokémon", widthDp = 390, heightDp = 844)
@Composable
private fun PokemonDetailsMissingPreview() {
    PokeApplicationTheme { PokemonDetailsContent(PokemonDetailsUiState(isLoading = false), {}, {}, {}) }
}

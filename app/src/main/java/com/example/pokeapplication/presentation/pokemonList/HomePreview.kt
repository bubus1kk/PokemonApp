package com.example.pokeapplication.presentation.pokemonlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonType
import com.example.pokeapplication.ui.theme.PokeApplicationTheme

// Sample data is used only by previews; HomeScreen always reads the ViewModel.
private val previewPokemons = listOf(
    previewPokemon(1, "bulbasaur", "grass", "poison"),
    previewPokemon(4, "charmander", "fire"),
    previewPokemon(7, "squirtle", "water"),
    previewPokemon(25, "pikachu", "electric"),
    previewPokemon(39, "jigglypuff", "normal", "fairy"),
    previewPokemon(10, "caterpie", "bug")
)

private fun previewPokemon(id: Int, name: String, vararg types: String) = Pokemon(
    id = id,
    name = name,
    imageUrl = "",
    height = 0,
    weight = 0,
    baseExperience = null,
    types = types.map { PokemonType(it) },
    abilities = emptyList(),
    stats = emptyList()
)

@Preview(name = "Home", widthDp = 390, heightDp = 844, showBackground = true)
@Preview(name = "Compact", widthDp = 320, heightDp = 640, showBackground = true)
@Preview(name = "Large text", widthDp = 390, heightDp = 844, fontScale = 1.3f)
@Preview(name = "Landscape", widthDp = 844, heightDp = 390)
@Composable
fun HomePreview() {
    var query by remember { mutableStateOf("") }
    PokeApplicationTheme {
        HomeContent(
            uiState = PokemonListUiState(
                pokemons = previewPokemons.filter {
                    it.name.contains(query, ignoreCase = true) || it.id.toString() == query
                },
                query = query
            ),
            onQueryChanged = { query = it },
            onRetry = {}
        )
    }
}

@Preview(name = "Loading", widthDp = 390, heightDp = 844)
@Composable
private fun HomeLoadingPreview() {
    PokeApplicationTheme {
        HomeContent(PokemonListUiState(isLoading = true), {}, {})
    }
}

@Preview(name = "Favorites", widthDp = 390, heightDp = 844)
@Preview(name = "Compact favorites", widthDp = 320, heightDp = 640)
@Composable
fun FavoritesPreview() {
    PokeApplicationTheme {
        HomeContent(
            uiState = PokemonListUiState(
                pokemons = previewPokemons.filter { it.id == 1 || it.id == 25 }.map { it.copy(isFavorite = true) },
                onlyFavorites = true
            ),
            onQueryChanged = {},
            onRetry = {}
        )
    }
}

@Preview(name = "Empty favorites", widthDp = 390, heightDp = 844)
@Composable
private fun EmptyFavoritesPreview() {
    PokeApplicationTheme {
        HomeContent(PokemonListUiState(onlyFavorites = true), {}, {})
    }
}

@Preview(name = "Error", widthDp = 390, heightDp = 844)
@Composable
private fun HomeErrorPreview() {
    PokeApplicationTheme {
        HomeContent(PokemonListUiState(errorMessage = "Unable to load Pokémon"), {}, {})
    }
}

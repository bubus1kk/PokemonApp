package com.example.pokeapplication.presentation.pokemonlist

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonSortOrder

data class PokemonListUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val query: String = "",
    val onlyFavorites: Boolean = false,
    val typeName: String? = null,
    val sortOrder: PokemonSortOrder = PokemonSortOrder.ID_ASC,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
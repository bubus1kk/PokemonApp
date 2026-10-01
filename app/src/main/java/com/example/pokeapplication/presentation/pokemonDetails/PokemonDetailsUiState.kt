package com.example.pokeapplication.presentation.pokemondetails

import com.example.pokeapplication.domain.model.Pokemon

enum class PokemonDetailsError { LOAD, FAVORITE }

data class PokemonDetailsUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = true,
    val isUpdatingFavorite: Boolean = false,
    val error: PokemonDetailsError? = null
)

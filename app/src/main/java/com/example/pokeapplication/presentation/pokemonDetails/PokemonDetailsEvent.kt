package com.example.pokeapplication.presentation.pokemondetails

sealed interface PokemonDetailsEvent {
    data object NoteSaved : PokemonDetailsEvent
}
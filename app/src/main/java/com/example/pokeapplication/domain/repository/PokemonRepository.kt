package com.example.pokeapplication.domain.repository

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonNote
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>

    fun getFavoritePokemons(): Flow<List<Pokemon>>

    suspend fun getPokemonById(pokemonId: Int): Pokemon?

    suspend fun toggleFavorite(pokemonId: Int)

    suspend fun refreshPokemons()
}
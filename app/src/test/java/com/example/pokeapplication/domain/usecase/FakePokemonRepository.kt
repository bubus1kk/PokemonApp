package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonType
import com.example.pokeapplication.domain.repository.PokemonRepository
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePokemonRepository : PokemonRepository {
    private val pokemons = MutableStateFlow<List<Pokemon>>(emptyList())

    var shouldThrowOnRefresh = false
    var refreshDelayMillis = 0L
    var refreshedPokemons: List<Pokemon>? = null
    var refreshCalls = 0
        private set

    fun setPokemons(list: List<Pokemon>) {
        pokemons.value = list
    }

    override fun getPokemonList(): Flow<List<Pokemon>> = pokemons

    override suspend fun getPokemonById(pokemonId: Int): Pokemon? =
        pokemons.value.firstOrNull { it.id == pokemonId }

    override suspend fun toggleFavorite(pokemonId: Int) {
        pokemons.value = pokemons.value.map { pokemon ->
            if (pokemon.id == pokemonId) pokemon.copy(isFavorite = !pokemon.isFavorite)
            else pokemon
        }
    }

    override suspend fun refreshPokemons() {
        refreshCalls++
        delay(refreshDelayMillis)
        if (shouldThrowOnRefresh) throw IOException("Ошибка обновления в тесте")
        refreshedPokemons?.let { setPokemons(it) }
    }
}

fun testPokemon(
    id: Int,
    name: String,
    vararg types: String,
    isFavorite: Boolean = false
) = Pokemon(
    id = id,
    name = name,
    imageUrl = "",
    height = 0,
    weight = 0,
    baseExperience = null,
    types = types.map { PokemonType(it) },
    abilities = emptyList(),
    stats = emptyList(),
    isFavorite = isFavorite
)

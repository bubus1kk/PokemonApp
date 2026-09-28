package com.example.pokeapplication.data.local

import androidx.room3.withWriteTransaction
import com.example.pokeapplication.data.local.mapper.toEntity
import com.example.pokeapplication.data.local.mapper.toTypeEntities
import com.example.pokeapplication.domain.model.Pokemon
import javax.inject.Inject
import com.example.pokeapplication.data.local.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonLocalDataSource @Inject constructor(private val database: PokemonDatabase) {
    private val pokemonDao = database.pokemonDao()

    suspend fun savePokemons(pokemons: List<Pokemon>) {
        database.withWriteTransaction {
            val favoriteIds = pokemonDao.getFavoriteIds().toSet()

            val entities = pokemons.map { pokemon ->
                pokemon.toEntity().copy(isFavorite = pokemon.id in favoriteIds)
            }

            pokemonDao.upsertPokemons(entities)

            for (pokemon in pokemons) {
                pokemonDao.deleteTypesByPokemonId(pokemon.id)

                val types = pokemon.toTypeEntities().distinctBy { it.typeName }

                if (types.isNotEmpty()) {
                    pokemonDao.insertTypes(types)
                }
            }
        }
    }

    fun observePokemons(): Flow<List<Pokemon>> {
        return pokemonDao.observePokemons().map { storedPokemons ->
            storedPokemons.map { storedPokemon ->
                storedPokemon.toDomain()
            }
        }
    }

    suspend fun getPokemonById(pokemonId: Int): Pokemon? {
        return pokemonDao.getPokemonById(pokemonId)?.toDomain()
    }

    suspend fun toggleFavorite(pokemonId: Int) {
        val updatedRows = pokemonDao.toggleFavorite(pokemonId)

        check(updatedRows == 1) { "Покемон с номером $pokemonId не найден" }
    }
}
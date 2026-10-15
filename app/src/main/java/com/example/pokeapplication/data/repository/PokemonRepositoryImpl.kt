package com.example.pokeapplication.data.repository

import com.example.pokeapplication.data.local.PokemonLocalDataSource
import com.example.pokeapplication.data.remote.PokemonRemoteDataSource
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl @Inject constructor(private val localDataSource: PokemonLocalDataSource,
    private val remoteDataSource: PokemonRemoteDataSource) : PokemonRepository {

    override fun getPokemonList(): Flow<List<Pokemon>> {
        return localDataSource.observePokemons()
    }

    override suspend fun getPokemonById(pokemonId: Int): Pokemon? {
        return localDataSource.getPokemonById(pokemonId)
    }

    override suspend fun toggleFavorite(pokemonId: Int) {
        localDataSource.toggleFavorite(pokemonId)
    }

    override suspend fun refreshPokemons() {
        val pokemons = remoteDataSource.getPokemons()
        localDataSource.savePokemons(pokemons)
    }
}

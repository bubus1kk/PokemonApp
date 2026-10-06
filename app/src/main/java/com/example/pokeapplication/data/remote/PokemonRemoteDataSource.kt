package com.example.pokeapplication.data.remote

import com.example.pokeapplication.data.remote.api.PokemonApi
import com.example.pokeapplication.data.remote.mapper.toDomain
import com.example.pokeapplication.domain.model.Pokemon
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PokemonRemoteDataSource @Inject constructor(private val api: PokemonApi) {
    suspend fun getPokemons(): List<Pokemon> = coroutineScope {
        val response = api.getPokemons(limit = 200, offset = 0)
        val pokemons = mutableListOf<Pokemon>()

        for (group in response.results.chunked(10)) {
            val loadedGroup = group.map { item ->
                async {
                    api.getPokemonDetails(item.name).toDomain()
                }
            }.awaitAll()

            pokemons.addAll(loadedGroup)
        }
        pokemons
    }
}
package com.example.pokeapplication.data.remote

import com.example.pokeapplication.data.remote.api.PokemonApi
import com.example.pokeapplication.data.remote.mapper.toDomain
import com.example.pokeapplication.domain.model.Pokemon
import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(private val api: PokemonApi) {

    suspend fun getPokemons(): List<Pokemon> {
        val response = api.getPokemons(limit = 200, offset = 0)

        return response.results.map { item ->
            api.getPokemonDetails(item.name).toDomain()
        }
    }
}
package com.example.pokeapplication.data.remote.api

import com.example.pokeapplication.data.remote.dto.PokemonDetailsDto
import com.example.pokeapplication.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemons(@Query("limit") limit: Int, @Query("offset") offset: Int): PokemonListResponseDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailsDto
}
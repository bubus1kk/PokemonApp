package com.example.pokeapplication.domain.repository

import com.example.pokeapplication.domain.model.PokemonPhoto

interface PokemonPhotoRepository {

    suspend fun getPhotos(pokemonId: Int): List<PokemonPhoto>

    suspend fun uploadPhoto(pokemonId: Int, sourceUri: String): PokemonPhoto

    suspend fun deletePhoto(photoId: String)
}
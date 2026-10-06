package com.example.pokeapplication.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonPhotoDto(
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("pokemon_id")
    val pokemonId: Int,

    @SerialName("storage_path")
    val storagePath: String,

    @SerialName("created_at")
    val createdAt: String
)
package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpritesDto(
    @SerialName("front_default")
    val frontDefault: String?
)
package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeDto(
    @SerialName("slot")
    val slot: Int,
    @SerialName("type")
    val type: TypeNameDto
)
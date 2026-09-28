package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListItemDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
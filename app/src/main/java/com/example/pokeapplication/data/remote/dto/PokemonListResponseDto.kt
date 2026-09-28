package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    @SerialName("results")
    val results: List<PokemonListItemDto>
)
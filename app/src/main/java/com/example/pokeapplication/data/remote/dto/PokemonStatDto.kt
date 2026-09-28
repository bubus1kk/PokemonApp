package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonStatDto(
    @SerialName("base_stat")
    val baseStat: Int,
    @SerialName("stat")
    val stat: StatNameDto
)
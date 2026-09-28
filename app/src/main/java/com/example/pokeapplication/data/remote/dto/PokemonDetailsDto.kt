package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailsDto(
    @SerialName("abilities")
    val abilities: List<PokemonAbilityDto>,
    @SerialName("base_experience")
    val baseExperience: Int?,
    @SerialName("height")
    val height: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("sprites")
    val sprites: PokemonSpritesDto,
    @SerialName("stats")
    val stats: List<PokemonStatDto>,
    @SerialName("types")
    val types: List<PokemonTypeDto>,
    @SerialName("weight")
    val weight: Int
)
package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonAbilityDto(
    @SerialName("ability")
    val ability: AbilityNameDto,
    @SerialName("is_hidden")
    val isHidden: Boolean
)
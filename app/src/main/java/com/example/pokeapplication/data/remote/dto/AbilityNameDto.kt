package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AbilityNameDto(
    @SerialName("name")
    val name: String
)
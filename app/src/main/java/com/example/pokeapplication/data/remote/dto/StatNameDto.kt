package com.example.pokeapplication.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatNameDto(
    @SerialName("name")
    val name: String
)
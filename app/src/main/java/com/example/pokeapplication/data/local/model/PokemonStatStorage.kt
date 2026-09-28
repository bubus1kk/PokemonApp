package com.example.pokeapplication.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonStatStorage(
    val name: String,
    val value: Int
)
package com.example.pokeapplication.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class AbilityStorage(
    val name: String,
    val isHidden: Boolean
)
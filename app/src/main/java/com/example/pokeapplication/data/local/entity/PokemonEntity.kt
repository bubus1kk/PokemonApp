package com.example.pokeapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int?,
    val abilitiesJson: String,
    val statsJson: String,
    val isFavorite: Boolean = false
)
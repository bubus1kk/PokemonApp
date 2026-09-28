package com.example.pokeapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "pokemons_notes")
data class PokemonNoteEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pokemonId: Int,
    val noteText: String
)
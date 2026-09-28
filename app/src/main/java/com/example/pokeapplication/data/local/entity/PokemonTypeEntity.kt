package com.example.pokeapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey

@Entity(tableName = "pokemons_types",
    primaryKeys =["pokemonId", "typeName"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns=["id"],
            childColumns= ["pokemonId"],
            onDelete = ForeignKey.CASCADE)])

data class PokemonTypeEntity(
    val pokemonId: Int,
    val typeName: String
)
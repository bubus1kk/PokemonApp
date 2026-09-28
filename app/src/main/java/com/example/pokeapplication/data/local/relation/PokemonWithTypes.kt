package com.example.pokeapplication.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.pokeapplication.data.local.entity.PokemonEntity
import com.example.pokeapplication.data.local.entity.PokemonTypeEntity

data class PokemonWithTypes(
    @Embedded
    val pokemon: PokemonEntity,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["pokemonId"]
    )
    val types: List<PokemonTypeEntity>
)


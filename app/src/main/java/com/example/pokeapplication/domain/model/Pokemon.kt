package com.example.pokeapplication.domain.model

data class Pokemon (
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int?,
    val types: List<PokemonType>,
    val abilities: List<Ability>,
    val stats: List<PokemonStat>,
    val isFavorite: Boolean = false
)
package com.example.pokeapplication.data.remote.mapper

import com.example.pokeapplication.data.remote.dto.PokemonDetailsDto
import com.example.pokeapplication.domain.model.Ability
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonStat
import com.example.pokeapplication.domain.model.PokemonType

fun PokemonDetailsDto.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.frontDefault.orEmpty(),
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.map { typeDto ->
            PokemonType(
                name = typeDto.type.name
            )
        },
        abilities = abilities.map { abilityDto ->
            Ability(
                name = abilityDto.ability.name,
                isHidden = abilityDto.isHidden
            )
        },
        stats = stats.map { statDto ->
            PokemonStat(
                name = statDto.stat.name,
                value = statDto.baseStat
            )
        }
    )
}
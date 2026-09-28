package com.example.pokeapplication.data.local.mapper

import com.example.pokeapplication.data.local.entity.PokemonEntity
import com.example.pokeapplication.data.local.entity.PokemonTypeEntity
import com.example.pokeapplication.data.local.model.AbilityStorage
import com.example.pokeapplication.data.local.model.PokemonStatStorage
import com.example.pokeapplication.data.local.relation.PokemonWithTypes
import com.example.pokeapplication.domain.model.Ability
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonStat
import com.example.pokeapplication.domain.model.PokemonType
import kotlinx.serialization.json.Json

fun PokemonWithTypes.toDomain(): Pokemon {
    val storedAbilities = Json.decodeFromString<List<AbilityStorage>>(pokemon.abilitiesJson)

    val storedStats = Json.decodeFromString<List<PokemonStatStorage>>(pokemon.statsJson)

    return Pokemon(
        id = pokemon.id,
        name = pokemon.name,
        imageUrl = pokemon.imageUrl,
        height = pokemon.height,
        weight = pokemon.weight,
        baseExperience = pokemon.baseExperience,
        types = types.map { type ->
            PokemonType(name = type.typeName)
        },
        abilities = storedAbilities.map { ability ->
            Ability(
                name = ability.name,
                isHidden = ability.isHidden
            )
        },
        stats = storedStats.map { stat ->
            PokemonStat(
                name = stat.name,
                value = stat.value
            )
        },
        isFavorite = pokemon.isFavorite
    )
}

fun Pokemon.toEntity(): PokemonEntity {
    val storedAbilities = abilities.map { ability ->
        AbilityStorage(
            name = ability.name,
            isHidden = ability.isHidden
        )
    }

    val storedStats = stats.map { stat ->
        PokemonStatStorage(
            name = stat.name,
            value = stat.value
        )
    }

    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        abilitiesJson = Json.encodeToString(storedAbilities),
        statsJson = Json.encodeToString(storedStats),
        isFavorite = isFavorite
    )
}

fun Pokemon.toTypeEntities(): List<PokemonTypeEntity> {
    return types.map { type ->
        PokemonTypeEntity(
            pokemonId = id,
            typeName = type.name
        )
    }
}
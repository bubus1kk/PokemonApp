package com.example.pokeapplication.data.local.mapper

import com.example.pokeapplication.data.local.entity.PokemonNoteEntity
import com.example.pokeapplication.domain.model.PokemonNote

fun PokemonNoteEntity.toDomain(): PokemonNote {
    return PokemonNote(
        id = this.id,
        pokemonId = this.pokemonId,
        noteText = this.noteText
    )
}

fun PokemonNote.toEntity(): PokemonNoteEntity {
    return PokemonNoteEntity(
        id = this.id,
        pokemonId = this.pokemonId,
        noteText = this.noteText
    )
}
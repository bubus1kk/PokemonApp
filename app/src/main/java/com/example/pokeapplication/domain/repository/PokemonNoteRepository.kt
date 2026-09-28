package com.example.pokeapplication.domain.repository

import com.example.pokeapplication.domain.model.PokemonNote
import kotlinx.coroutines.flow.Flow

interface PokemonNoteRepository {

    suspend fun saveNote(note: PokemonNote): Int

    fun getNotes(pokemonId: Int): Flow<List<PokemonNote>>

    suspend fun getNoteById(noteId: Int): PokemonNote?

    suspend fun deleteNote(noteId: Int)
}
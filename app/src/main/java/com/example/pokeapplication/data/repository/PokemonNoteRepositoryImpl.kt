package com.example.pokeapplication.data.repository

import androidx.room3.withWriteTransaction
import com.example.pokeapplication.data.local.PokemonDatabase
import com.example.pokeapplication.data.local.mapper.toDomain
import com.example.pokeapplication.data.local.mapper.toEntity
import com.example.pokeapplication.domain.model.PokemonNote
import com.example.pokeapplication.domain.repository.PokemonNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonNoteRepositoryImpl @Inject constructor(private val database: PokemonDatabase) : PokemonNoteRepository {

    private val noteDao = database.pokemonNoteDao()

    override suspend fun saveNote(note: PokemonNote): Int {
        require(note.id >= 0) { "ID заметки не может быть отрицательным" }

        return database.withWriteTransaction {
            if (note.id == 0) {
                val insertedId = noteDao.insertNote(note.toEntity())
                Math.toIntExact(insertedId)
            } else {
                val updatedRows = noteDao.updateNoteText(noteId = note.id, noteText = note.noteText)

                check(updatedRows == 1) { "Заметка с ID ${note.id} не найдена" }

                note.id
            }
        }
    }

    override fun getNotes(pokemonId: Int): Flow<List<PokemonNote>> {
        return noteDao.observeNotes(pokemonId).map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getNoteById(noteId: Int): PokemonNote? {
        return noteDao.getNoteById(noteId)?.toDomain()
    }

    override suspend fun deleteNote(noteId: Int) {
        noteDao.deleteNote(noteId)
    }
}
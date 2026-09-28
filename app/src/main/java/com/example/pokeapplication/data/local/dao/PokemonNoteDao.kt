package com.example.pokeapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.pokeapplication.data.local.entity.PokemonNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonNoteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(note: PokemonNoteEntity): Long

    @Query("SELECT * FROM pokemons_notes where pokemonId = :pokemonId ORDER BY id DESC")
    fun observeNotes(pokemonId: Int): Flow<List<PokemonNoteEntity>>

    @Query("SELECT * FROM pokemons_notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): PokemonNoteEntity?

    @Query("UPDATE pokemons_notes SET noteText = :noteText WHERE id = :noteId")
    suspend fun updateNoteText(noteId: Int, noteText: String): Int

    @Query("DELETE FROM pokemons_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int): Int

}
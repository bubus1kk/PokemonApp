package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.PokemonNote
import com.example.pokeapplication.domain.repository.PokemonNoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor (private val repository: PokemonNoteRepository) {
    suspend operator fun invoke(note: PokemonNote) : Int{
        require(!note.noteText.isBlank()){
            "Текст заметки не может быть пустым"
        }
        return repository.saveNote(note)
    }
}
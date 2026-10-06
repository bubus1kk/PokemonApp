package com.example.pokeapplication.presentation.pokemondetails

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonNote

enum class PokemonDetailsError { LOAD, FAVORITE }

data class PokemonDetailsUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = true,
    val isUpdatingFavorite: Boolean = false,
    val error: PokemonDetailsError? = null,
    val notes: List<PokemonNote> = emptyList(),
    val isNotesLoading: Boolean = true,
    val notesErrorMessage: String? = null,
    val showNoteEditor: Boolean = false,
    val noteText: String = "",
    val isSavingNote: Boolean = false,
    val noteFormErrorMessage: String? = null,
    val editingNoteId: Int? = null,
    val deletingNoteId: Int? = null,
    val noteActionErrorMessage: String? = null
)

package com.example.pokeapplication.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pokeapplication.domain.model.PokemonNote
import com.example.pokeapplication.domain.usecase.GetPokemonByIdUseCase
import com.example.pokeapplication.domain.usecase.ToggleFavoriteUseCase
import com.example.pokeapplication.presentation.navigation.PokemonDetailsRoute
import com.example.pokeapplication.presentation.pokemondetails.PokemonDetailsError
import com.example.pokeapplication.presentation.pokemondetails.PokemonDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.pokeapplication.domain.repository.PokemonNoteRepository
import com.example.pokeapplication.domain.usecase.SaveNoteUseCase
import com.example.pokeapplication.presentation.pokemondetails.PokemonDetailsEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val noteRepository: PokemonNoteRepository,
    savedStateHandle: SavedStateHandle,
    private val getPokemonById: GetPokemonByIdUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {
    private val pokemonId = savedStateHandle.toRoute<PokemonDetailsRoute>().pokemonId
    private val _uiState = MutableStateFlow(PokemonDetailsUiState())

    private val _events = Channel<PokemonDetailsEvent>(Channel.BUFFERED)
    val uiState = _uiState.asStateFlow()

    val events = _events.receiveAsFlow()
    private var loadJob: Job? = null
    private var favoriteJob: Job? = null

    private var notesJob: Job? = null

    private var saveNoteJob: Job? = null

    private var deleteNoteJob: Job? = null

    init {
        loadPokemon()
        observeNotes()
    }

    fun loadPokemon() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val pokemon = getPokemonById(pokemonId)
                _uiState.update { it.copy(pokemon = pokemon) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(error = PokemonDetailsError.LOAD) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFavoriteClick() {
        if (favoriteJob?.isActive == true) return
        val pokemon = _uiState.value.pokemon ?: return
        favoriteJob = viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingFavorite = true, error = null) }
            try {
                toggleFavorite(pokemonId)
                _uiState.update {
                    it.copy(pokemon = pokemon.copy(isFavorite = !pokemon.isFavorite))
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(error = PokemonDetailsError.FAVORITE) }
            } finally {
                _uiState.update { it.copy(isUpdatingFavorite = false) }
            }
        }
    }

    fun observeNotes() {
        notesJob?.cancel()

        notesJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isNotesLoading = true,
                    notesErrorMessage = null
                )
            }

            try {
                noteRepository.getNotes(pokemonId).collect { notes ->
                    _uiState.update {
                        it.copy(
                            notes = notes,
                            isNotesLoading = false
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isNotesLoading = false,
                        notesErrorMessage = "Не удалось загрузить заметки"
                    )
                }
            }
        }
    }

    fun onAddNoteClick() {
        if (_uiState.value.isSavingNote) return
        if (_uiState.value.pokemon == null) return
        if (_uiState.value.deletingNoteId != null) return
        _uiState.update {
            it.copy(
                showNoteEditor = true,
                noteText = "",
                noteFormErrorMessage = null,
                editingNoteId = null
            )
        }
    }

    fun onNoteTextChanged(text: String) {
        if (_uiState.value.isSavingNote) return

        _uiState.update {
            it.copy(
                noteText = text,
                noteFormErrorMessage = null
            )
        }
    }

    fun onNoteEditorDismiss() {
        if (_uiState.value.isSavingNote) return

        _uiState.update {
            it.copy(
                showNoteEditor = false,
                noteText = "",
                noteFormErrorMessage = null,
                editingNoteId = null
            )
        }
    }

    fun onSaveNote() {
        if (saveNoteJob?.isActive == true) return

        val state = _uiState.value
        if (!state.showNoteEditor || state.pokemon == null) return

        val text = state.noteText.trim()

        saveNoteJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSavingNote = true,
                    noteFormErrorMessage = null
                )
            }

            try {
                saveNoteUseCase(
                    PokemonNote(
                        id = state.editingNoteId ?: 0,
                        pokemonId = pokemonId,
                        noteText = text
                    )
                )

                _uiState.update {
                    it.copy(
                        showNoteEditor = false,
                        noteText = "",
                        noteFormErrorMessage = null,
                        editingNoteId = null
                    )
                }

                _events.send(PokemonDetailsEvent.NoteSaved)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IllegalArgumentException) {
                _uiState.update {
                    it.copy(
                        noteFormErrorMessage = exception.message
                            ?: "Некорректные данные заметки"
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        noteFormErrorMessage = "Не удалось сохранить заметку"
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isSavingNote = false)
                }
            }
        }
    }

    fun onEditNoteClick(noteId: Int) {
        val state = _uiState.value

        if (state.isSavingNote || state.deletingNoteId != null) return

        val note = state.notes.firstOrNull { it.id == noteId } ?: return

        _uiState.update {
            it.copy(
                showNoteEditor = true,
                editingNoteId = note.id,
                noteText = note.noteText,
                noteFormErrorMessage = null
            )
        }
    }

    fun onDeleteNoteClick(noteId: Int) {
        if (deleteNoteJob?.isActive == true) return

        val state = _uiState.value

        if (state.isSavingNote || state.showNoteEditor) return
        if (state.notes.none { it.id == noteId }) return

        deleteNoteJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deletingNoteId = noteId,
                    noteActionErrorMessage = null
                )
            }

            try {
                noteRepository.deleteNote(noteId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        noteActionErrorMessage = "Не удалось удалить заметку"
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(deletingNoteId = null)
                }
            }
        }
    }
}

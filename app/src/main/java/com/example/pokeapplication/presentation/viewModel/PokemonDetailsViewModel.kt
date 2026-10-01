package com.example.pokeapplication.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonById: GetPokemonByIdUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {
    private val pokemonId = savedStateHandle.toRoute<PokemonDetailsRoute>().pokemonId
    private val _uiState = MutableStateFlow(PokemonDetailsUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null
    private var favoriteJob: Job? = null

    init {
        loadPokemon()
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
}

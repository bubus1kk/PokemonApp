package com.example.pokeapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.domain.usecase.GetPokemonListUseCase
import com.example.pokeapplication.domain.usecase.RefreshPokemonUseCase
import com.example.pokeapplication.domain.usecase.ToggleFavoriteUseCase
import com.example.pokeapplication.presentation.pokemonlist.PokemonListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonList: GetPokemonListUseCase,
    private val refreshPokemon: RefreshPokemonUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    // Each navigation destination owns its list state. Favorites is a fixed list mode.
    private val favoritesOnly = savedStateHandle.get<Boolean>("onlyFavorites") ?: false
    private val _uiState = MutableStateFlow(PokemonListUiState(onlyFavorites = favoritesOnly))
    val uiState = _uiState.asStateFlow()

    private var observeJob: Job? = null
    private var refreshJob: Job? = null

    init {
        observePokemons()
        if (!favoritesOnly) onRefresh()
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        observePokemons()
    }

    fun onTypeChanged(typeName: String?) {
        _uiState.update { it.copy(typeName = typeName) }
        observePokemons()
    }

    fun onSortOrderChanged(sortOrder: PokemonSortOrder) {
        _uiState.update { it.copy(sortOrder = sortOrder) }
        observePokemons()
    }

    fun onRefresh() {
        if (favoritesOnly) {
            observePokemons()
            return
        }
        if (refreshJob?.isActive == true) return

        if (observeJob?.isActive != true) {
            observePokemons()
        }

        refreshJob = viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                refreshPokemon()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Не удалось обновить покемонов"
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFavoriteClicked(pokemonId: Int) {
        viewModelScope.launch {
            try {
                toggleFavorite(pokemonId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Не удалось изменить избранное"
                    )
                }
            }
        }
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun observePokemons() {
        observeJob?.cancel()

        val currentState = _uiState.value

        observeJob = viewModelScope.launch {
            if (favoritesOnly) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }
            try {
                getPokemonList(
                    query = currentState.query,
                    onlyFavorites = currentState.onlyFavorites,
                    typeName = currentState.typeName,
                    sortOrder = currentState.sortOrder
                ).collect { pokemons ->
                    _uiState.update {
                        it.copy(pokemons = pokemons,
                            isLoading = if (favoritesOnly) false else it.isLoading)
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Не удалось прочитать список покемонов",
                        isLoading = if (favoritesOnly) false else it.isLoading
                    )
                }
            }
        }
    }
}

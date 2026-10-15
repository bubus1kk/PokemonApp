package com.example.pokeapplication.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.pokeapplication.MainDispatcherRule
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.domain.usecase.FakePokemonRepository
import com.example.pokeapplication.domain.usecase.GetPokemonListUseCase
import com.example.pokeapplication.domain.usecase.RefreshPokemonUseCase
import com.example.pokeapplication.domain.usecase.ToggleFavoriteUseCase
import com.example.pokeapplication.domain.usecase.testPokemon
import com.example.pokeapplication.presentation.viewModel.PokemonListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakePokemonRepository
    private val viewModels = mutableListOf<PokemonListViewModel>()

    @Before
    fun setUp() {
        repository = FakePokemonRepository().apply {
            setPokemons(listOf(
                testPokemon(25, "pikachu", "electric"),
                testPokemon(1, "bulbasaur", "grass", isFavorite = true)
            ))
        }
    }

    private fun createViewModel(handle: SavedStateHandle = SavedStateHandle()) =
        PokemonListViewModel(handle, GetPokemonListUseCase(repository),
            RefreshPokemonUseCase(repository), ToggleFavoriteUseCase(repository))
            .also { viewModels.add(it) }

    @After
    fun tearDown() {
        viewModels.forEach { it.viewModelScope.cancel() }
    }

    //после загрузки появляется сортированный по умолчанию список и индикатор пропадает
    @Test
    fun loadingCompletesWithASortedListAndNoLoadingIndicator() = runTest {
        val viewModel = createViewModel()

        advanceUntilIdle()

        assertEquals(listOf(1, 25), viewModel.uiState.value.pokemons.map { it.id })
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, repository.refreshCalls)
    }

    //пока данные пустые показываем загрузку
    @Test
    fun emptyCacheShowsLoadingUntilRefreshCompletes() = runTest {
        repository.setPokemons(emptyList())
        repository.refreshDelayMillis = 1_000
        repository.refreshedPokemons = listOf(testPokemon(7, "squirtle", "water"))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)
            runCurrent()
            assertTrue(viewModel.uiState.value.isLoading)

            advanceUntilIdle()

            val loaded = expectMostRecentItem()
            assertFalse(loaded.isLoading)
            assertEquals(listOf(7), loaded.pokemons.map { it.id })
            cancelAndIgnoreRemainingEvents()
        }
    }


    //сохраненные данные показываются во время обновления списка
    @Test
    fun cachedDataRemainsVisibleDuringBackgroundRefresh() = runTest {
        repository.refreshDelayMillis = 1_000
        val viewModel = createViewModel()

        runCurrent()

        assertEquals(listOf(1, 25), viewModel.uiState.value.pokemons.map { it.id })
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, repository.refreshCalls)
        advanceUntilIdle()
    }

    //изменение списка автоматически обновит экран
    @Test
    fun repositoryChangesUpdateStateWithoutManualReload() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        repository.setPokemons(listOf(testPokemon(7, "squirtle", "water")))
        runCurrent()

        assertEquals(listOf(7), viewModel.uiState.value.pokemons.map { it.id })
        assertEquals(1, repository.refreshCalls)
    }

    //сохранение данных при ошибке сети
    @Test
    fun refreshFailurePreservesCachedDataAndStopsLoading() = runTest {
        repository.shouldThrowOnRefresh = true
        val viewModel = createViewModel()

        advanceUntilIdle()

        assertEquals(listOf(1, 25), viewModel.uiState.value.pokemons.map { it.id })
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    //поиск,сортировка и типы выполняются без обновления данных из сети
    @Test
    fun searchTypeAndSortingUpdateStateWithoutAnotherNetworkRefresh() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onQueryChanged("pika")
        runCurrent()
        assertEquals("pika", viewModel.uiState.value.query)
        assertEquals(listOf(25), viewModel.uiState.value.pokemons.map { it.id })

        viewModel.onQueryChanged("")
        viewModel.onTypeChanged("grass")
        runCurrent()
        assertEquals("grass", viewModel.uiState.value.typeName)
        assertEquals(listOf(1), viewModel.uiState.value.pokemons.map { it.id })

        viewModel.onTypeChanged(null)
        viewModel.onSortOrderChanged(PokemonSortOrder.ID_DESC)
        runCurrent()
        assertEquals(PokemonSortOrder.ID_DESC, viewModel.uiState.value.sortOrder)
        assertEquals(listOf(25, 1), viewModel.uiState.value.pokemons.map { it.id })
        assertEquals(1, repository.refreshCalls)
    }

    //проверка добавления/удаления из избранного
    @Test
    fun togglingFavoriteTwiceRestoresTheOriginalValue() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onFavoriteClicked(25)
        runCurrent()
        assertTrue(viewModel.uiState.value.pokemons.first { it.id == 25 }.isFavorite)

        viewModel.onFavoriteClicked(25)
        runCurrent()
        assertFalse(viewModel.uiState.value.pokemons.first { it.id == 25 }.isFavorite)
    }

    //работа избранного и типов без сети
    @Test
    fun favoritesAndTypePagesFilterLocalDataWithoutNetworkRequests() = runTest {
        val favorites = createViewModel(SavedStateHandle(mapOf("onlyFavorites" to true)))
        val electric = createViewModel(SavedStateHandle(mapOf("typeName" to "electric")))

        advanceUntilIdle()

        assertEquals(listOf(1), favorites.uiState.value.pokemons.map { it.id })
        assertEquals(listOf(25), electric.uiState.value.pokemons.map { it.id })
        assertFalse(favorites.uiState.value.isLoading)
        assertFalse(electric.uiState.value.isLoading)
        assertEquals(0, repository.refreshCalls)
    }
}

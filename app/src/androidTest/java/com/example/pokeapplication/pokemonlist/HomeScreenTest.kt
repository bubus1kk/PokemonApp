package com.example.pokeapplication.pokemonlist

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.domain.model.PokemonType
import com.example.pokeapplication.presentation.pokemonlist.HomeContent
import com.example.pokeapplication.presentation.pokemonlist.PokemonListUiState
import com.example.pokeapplication.ui.theme.PokeApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val pokemon = Pokemon(
        id = 1, name = "bulbasaur", imageUrl = "", height = 0, weight = 0,
        baseExperience = null, types = listOf(PokemonType("grass"), PokemonType("poison")),
        abilities = emptyList(), stats = emptyList()
    )

    private fun setContent(
        state: PokemonListUiState = PokemonListUiState(pokemons = listOf(pokemon)),
        onPokemonClick: (Int) -> Unit = {},
        onQueryChanged: (String) -> Unit = {},
        onTypeChanged: (String?) -> Unit = {},
        onSortOrderChanged: (PokemonSortOrder) -> Unit = {},
        onResetFilters: () -> Unit = {},
        onHomeClick: () -> Unit = {},
        onFavoritesClick: () -> Unit = {},
        onTypesClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            PokeApplicationTheme {
                HomeContent(
                    uiState = state,
                    onQueryChanged = onQueryChanged,
                    onPokemonClick = onPokemonClick,
                    onTypeChanged = onTypeChanged,
                    onSortOrderChanged = onSortOrderChanged,
                    onResetFilters = onResetFilters,
                    onHomeClick = onHomeClick,
                    onFavoritesClick = onFavoritesClick,
                    onTypesClick = onTypesClick
                )
            }
        }
    }

    //карточка отображает имя номер и тип покемона
    @Test
    fun cardDisplaysNameNumberAndTypes() {
        setContent()

        composeTestRule.onNodeWithText("PokeApp").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("#001").assertIsDisplayed()
        composeTestRule.onNodeWithText("GRASS · POISON").assertIsDisplayed()
    }

    //при нажатии на карточку передается правильный id покемона
    @Test
    fun cardClickPassesPokemonId() {
        var clickedId: Int? = null
        setContent(onPokemonClick = { clickedId = it })

        composeTestRule.onNodeWithText("Bulbasaur").performClick()

        assertEquals(1, clickedId)
    }

    //поиск выполняется
    @Test
    fun searchPassesEnteredText() {
        var enteredQuery = ""
        setContent(onQueryChanged = { enteredQuery = it })

        composeTestRule.onNodeWithContentDescription("Поиск покемонов").performTextInput("bulba")

        assertEquals("bulba", enteredQuery)
    }

    //проверка всех фильтров
    @Test
    fun filtersOpenPassSelectionsResetAndClose() {
        var selectedType: String? = null
        val selectedOrders = mutableListOf<PokemonSortOrder>()
        var resetCalled = false
        setContent(
            state = PokemonListUiState(typeName = "water"),
            onTypeChanged = { selectedType = it },
            onSortOrderChanged = { selectedOrders.add(it) },
            onResetFilters = { resetCalled = true }
        )

        composeTestRule.onNodeWithContentDescription("Фильтры").performClick()
        composeTestRule.onNodeWithText("Фильтры").assertIsDisplayed()
        composeTestRule.onNodeWithText("Grass").performClick()
        assertEquals("grass", selectedType)

        val labels = listOf("Номер ↑", "Номер ↓", "A–Z", "Z–A")
        labels.forEach { composeTestRule.onNodeWithText(it).performScrollTo().performClick() }
        assertEquals(PokemonSortOrder.entries.toList(), selectedOrders)

        composeTestRule.onNodeWithText("Сбросить").performScrollTo().performClick()
        assertTrue(resetCalled)
        composeTestRule.onNodeWithContentDescription("Закрыть фильтры").performClick()
        composeTestRule.onNodeWithText("Фильтры").assertDoesNotExist()
    }

    //показывается загурзка когда список пустой
    @Test
    fun loadingShowsIndicatorInsteadOfEmptyMessage() {
        setContent(PokemonListUiState(isLoading = true))

        composeTestRule.onNodeWithContentDescription("Загрузка покемонов").assertIsDisplayed()
        composeTestRule.onNodeWithText("Покемоны не найдены").assertDoesNotExist()
        composeTestRule.onNodeWithText("Bulbasaur").assertDoesNotExist()
    }

    //ошибка и пустой список показывают разные сообщения
    @Test
    fun errorAndEmptyResultHaveDifferentMessages() {
        val state = mutableStateOf(PokemonListUiState(errorMessage = "Ошибка загрузки"))
        composeTestRule.setContent {
            PokeApplicationTheme { HomeContent(uiState = state.value, onQueryChanged = {}) }
        }

        composeTestRule.onNodeWithText("Ошибка загрузки").assertIsDisplayed()
        composeTestRule.onNodeWithText("Покемоны не найдены").assertDoesNotExist()


        composeTestRule.runOnIdle { state.value = PokemonListUiState() }

        composeTestRule.onNodeWithText("Ошибка загрузки").assertDoesNotExist()
        composeTestRule.onNodeWithText("Покемоны не найдены").assertIsDisplayed()
    }

    //навбар перемещает в нужное место
    @Test
    fun bottomBarCallsEachNavigationCallback() {
        val destinations = mutableListOf<String>()
        setContent(
            onHomeClick = { destinations.add("home") },
            onFavoritesClick = { destinations.add("favorites") },
            onTypesClick = { destinations.add("types") }
        )

        composeTestRule.onNodeWithText("Главная").performClick()
        composeTestRule.onNodeWithText("Избранное").performClick()
        composeTestRule.onNodeWithText("Типы").performClick()

        assertEquals(listOf("home", "favorites", "types"), destinations)
    }
}

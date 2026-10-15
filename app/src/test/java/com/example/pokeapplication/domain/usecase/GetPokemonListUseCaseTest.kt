package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.PokemonSortOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPokemonListUseCaseTest {
    private lateinit var repository: FakePokemonRepository
    private lateinit var useCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        repository = FakePokemonRepository().apply {
            setPokemons(listOf(
                testPokemon(25, "pikachu", "electric", isFavorite = true),
                testPokemon(7, "squirtle", "water"),
                testPokemon(1, "bulbasaur", "grass", "poison", isFavorite = true)
            ))
        }
        useCase = GetPokemonListUseCase(repository)
    }

    //поиск по имени без учета регистра и пробелов
    @Test
    fun nameSearchIgnoresCaseAndSurroundingWhitespace() = runTest {
        val result = useCase(query = "  BULBA  ").first()

        assertEquals(listOf(1), result.map { it.id })
    }

    //поиск по номеру
    @Test
    fun numberSearchFindsAnExactMatch() = runTest {
        repository.setPokemons(listOf(testPokemon(1, "bulbasaur"), testPokemon(10, "caterpie")))

        val result = useCase(query = "1").first()

        assertEquals(listOf(1), result.map { it.id })
    }

    //в избранном только нужные покемоны
    @Test
    fun favoritesFilterExcludesOtherPokemon() = runTest {
        val result = useCase(onlyFavorites = true).first()

        assertEquals(listOf(1, 25), result.map { it.id })
    }

    //фильтрация по типу учитывает оба типа
    @Test
    fun typeFilterMatchesTheSecondTypeIgnoringCase() = runTest {
        val result = useCase(typeName = "POISON").first()

        assertEquals(listOf(1), result.map { it.id })
    }

    //все сортировки
    @Test
    fun eachSortOrderSortsTheEntireList() = runTest {
        val expected = mapOf(
            PokemonSortOrder.ID_ASC to listOf(1, 7, 25),
            PokemonSortOrder.ID_DESC to listOf(25, 7, 1),
            PokemonSortOrder.NAME_ASC to listOf(1, 25, 7),
            PokemonSortOrder.NAME_DESC to listOf(7, 25, 1)
        )

        expected.forEach { (order, ids) ->
            assertEquals(order.name, ids, useCase(sortOrder = order).first().map { it.id })
        }
    }


    //сортировка фильтр и поиск вместе
    @Test
    fun searchFiltersAndSortingWorkTogether() = runTest {
        repository.setPokemons(listOf(
            testPokemon(1, "bulbasaur", "grass", isFavorite = true),
            testPokemon(2, "ivysaur", "grass", isFavorite = true),
            testPokemon(3, "venusaur", "grass"),
            testPokemon(4, "charmander", "fire", isFavorite = true)
        ))

        val result = useCase(query = "saur", onlyFavorites = true,
            typeName = "grass", sortOrder = PokemonSortOrder.ID_DESC).first()

        assertEquals(listOf(2, 1), result.map { it.id })
    }

    //пустой результат
    @Test
    fun emptySourceAndUnmatchedQueryReturnAnEmptyList() = runTest {
        assertEquals(emptyList<Int>(), useCase(query = "missing").first().map { it.id })

        repository.setPokemons(emptyList())

        assertEquals(emptyList<Int>(), useCase().first().map { it.id })
    }
}

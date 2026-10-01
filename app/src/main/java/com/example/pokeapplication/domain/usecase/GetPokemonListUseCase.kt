package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonListUseCase @Inject constructor(private val repository: PokemonRepository) {

    operator fun invoke(
        query: String = "",
        onlyFavorites: Boolean = false,
        typeName: String? = null,
        sortOrder: PokemonSortOrder = PokemonSortOrder.ID_ASC
    ): Flow<List<Pokemon>> {
        val searchQuery = query.trim()
        val searchedId = searchQuery.toIntOrNull()

        return repository.getPokemonList().map { pokemons ->
            val filteredPokemons = pokemons.filter { pokemon ->
                val matchesSearch = when {
                    searchQuery.isEmpty() -> true
                    searchedId != null -> pokemon.id == searchedId
                    else -> pokemon.name.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                }

                val matchesFavorite = !onlyFavorites || pokemon.isFavorite

                val matchesType = typeName == null || pokemon.types.any { type ->
                        type.name.equals(typeName, ignoreCase = true)
                    }

                matchesSearch && matchesFavorite && matchesType
            }

            when (sortOrder) {
                PokemonSortOrder.ID_ASC ->
                    filteredPokemons.sortedBy { it.id }

                PokemonSortOrder.ID_DESC ->
                    filteredPokemons.sortedByDescending { it.id }

                PokemonSortOrder.NAME_ASC ->
                    filteredPokemons.sortedBy { it.name.lowercase() }

                PokemonSortOrder.NAME_DESC ->
                    filteredPokemons.sortedByDescending {
                        it.name.lowercase()
                    }
            }
        }
    }
}
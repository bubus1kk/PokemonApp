package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(): Flow<List<Pokemon>> {
        return repository.getPokemonList()
    }
}
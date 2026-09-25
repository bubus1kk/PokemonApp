package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonByIdUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemonId: Int): Pokemon? {
        return repository.getPokemonById(pokemonId)
    }
}
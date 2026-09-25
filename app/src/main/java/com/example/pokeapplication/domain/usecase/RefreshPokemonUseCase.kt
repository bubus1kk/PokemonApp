package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.repository.PokemonRepository
import javax.inject.Inject

class RefreshPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke (){
        repository.refreshPokemons()
    }
}
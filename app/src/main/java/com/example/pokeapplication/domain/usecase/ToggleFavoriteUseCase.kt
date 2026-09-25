package com.example.pokeapplication.domain.usecase

import com.example.pokeapplication.domain.repository.PokemonRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemonId:Int){
        repository.toggleFavorite(pokemonId)
    }
}
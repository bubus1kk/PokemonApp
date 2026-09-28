package com.example.pokeapplication.data.di

import com.example.pokeapplication.data.repository.PokemonNoteRepositoryImpl
import com.example.pokeapplication.domain.repository.PokemonNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPokemonNoteRepository(implementation: PokemonNoteRepositoryImpl): PokemonNoteRepository
}
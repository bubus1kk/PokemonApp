package com.example.pokeapplication.data.di

import com.example.pokeapplication.data.repository.PokemonNoteRepositoryImpl
import com.example.pokeapplication.data.repository.PokemonRepositoryImpl
import com.example.pokeapplication.domain.repository.PokemonNoteRepository
import com.example.pokeapplication.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.pokeapplication.data.repository.PokemonPhotoRepositoryImpl
import com.example.pokeapplication.domain.repository.PokemonPhotoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPokemonNoteRepository(implementation: PokemonNoteRepositoryImpl): PokemonNoteRepository

    @Binds
    abstract fun bindPokemonRepository(implementation: PokemonRepositoryImpl): PokemonRepository

    @Binds
    abstract fun bindPokemonPhotoRepository(implementation: PokemonPhotoRepositoryImpl): PokemonPhotoRepository
}
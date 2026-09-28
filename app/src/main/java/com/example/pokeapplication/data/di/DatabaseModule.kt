package com.example.pokeapplication.data.di

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.pokeapplication.data.local.PokemonDatabase
import com.example.pokeapplication.data.local.dao.PokemonDao
import com.example.pokeapplication.data.local.dao.PokemonNoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon.db"
        )
            .setDriver(AndroidSQLiteDriver())
            .build()
    }

    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides fun providePokemonNoteDao(
        database: PokemonDatabase): PokemonNoteDao {
        return database.pokemonNoteDao()
    }
}
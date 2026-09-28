package com.example.pokeapplication.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.pokeapplication.data.local.dao.PokemonDao
import com.example.pokeapplication.data.local.dao.PokemonNoteDao
import com.example.pokeapplication.data.local.entity.PokemonEntity
import com.example.pokeapplication.data.local.entity.PokemonNoteEntity
import com.example.pokeapplication.data.local.entity.PokemonTypeEntity

@Database(
    entities = [
        PokemonEntity::class,
        PokemonTypeEntity::class,
        PokemonNoteEntity::class
    ],
    version = 1
)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    abstract fun pokemonNoteDao(): PokemonNoteDao
}
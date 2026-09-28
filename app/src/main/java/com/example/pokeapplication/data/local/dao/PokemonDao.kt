package com.example.pokeapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.example.pokeapplication.data.local.entity.PokemonEntity
import com.example.pokeapplication.data.local.entity.PokemonTypeEntity
import com.example.pokeapplication.data.local.relation.PokemonWithTypes
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Transaction
    @Query("SELECT * FROM pokemons ORDER BY id ASC")
    fun observePokemons(): Flow<List<PokemonWithTypes>>

    @Transaction
    @Query("SELECT * FROM pokemons WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): PokemonWithTypes?

    @Query("UPDATE pokemons SET isFavorite = NOT isFavorite WHERE id = :pokemonId")
    suspend fun toggleFavorite(pokemonId: Int): Int

    @Upsert
    suspend fun upsertPokemons(pokemons: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTypes(types: List<PokemonTypeEntity>)

    @Query("DELETE FROM pokemons_types WHERE pokemonId = :pokemonId")
    suspend fun deleteTypesByPokemonId(pokemonId: Int)

    @Query("SELECT id FROM pokemons WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>
}
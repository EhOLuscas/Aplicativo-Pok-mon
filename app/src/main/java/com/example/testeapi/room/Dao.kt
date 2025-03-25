package com.example.testeapi.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getFavorites(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM pokemon WHERE name = :pokemonName)")
    fun isPokemonSaved(pokemonName: String): Flow<Boolean>
}
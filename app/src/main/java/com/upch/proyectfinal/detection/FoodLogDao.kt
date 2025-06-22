package com.upch.proyectfinal.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Insert
    suspend fun insert(log: FoodLog)

    @Query("SELECT * FROM FoodLog ORDER BY dateTime DESC")
    fun getAll(): Flow<List<FoodLog>>

    @Query("DELETE FROM FoodLog")
    suspend fun deleteAll()
}

package com.upch.proyectfinal.profile

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserData)

    @Query("SELECT * FROM user_data LIMIT 1")
    suspend fun getUser(): UserData?
}

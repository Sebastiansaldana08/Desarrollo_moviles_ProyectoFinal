package com.upch.proyectfinal.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gender: String,
    val age: Int,
    val height: Float,
    val weight: Float
)

package com.upch.proyectfinal.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodLabel: String,
    val calories: Int,
    val dateTime: String
)

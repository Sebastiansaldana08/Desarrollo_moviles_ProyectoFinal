package com.upch.proyectfinal.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodLog::class], version = 1, exportSchema = false)
abstract class FoodLogDatabase : RoomDatabase() {

    abstract fun foodLogDao(): FoodLogDao

    companion object {
        @Volatile
        private var INSTANCE: FoodLogDatabase? = null

        fun getInstance(context: Context): FoodLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodLogDatabase::class.java,
                    "food_log_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

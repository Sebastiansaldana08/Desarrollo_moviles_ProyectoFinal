package com.upch.proyectfinal.detection

object CalorieMap {
    private val calorieTable = mapOf(
        "rice" to 130,
        "fries potatoes" to 312,
        "tomatoes" to 18,
        "onion" to 40,
        "meat" to 250
    )

    fun getCaloriesFor(foodName: String): Int {
        return calorieTable[foodName.lowercase()] ?: 0
    }
}

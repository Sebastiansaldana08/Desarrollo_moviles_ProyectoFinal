package com.upch.proyectfinal.detection

import android.content.Context
import android.net.Uri
import kotlin.random.Random

val caloriasPorAlimento = mapOf(
    "rice" to 130,
    "fries potatoes" to 312,
    "tomatoes" to 18,
    "onion" to 40,
    "meat" to 250
)

object DetectionService {
    suspend fun detectarAlimentos(context: Context, imageUri: Uri): List<FoodItem> {
        val ingredientesSimulados = listOf("rice", "fries potatoes", "tomatoes", "onion", "meat")

        return ingredientesSimulados.map { ingrediente ->
            FoodItem(
                label = ingrediente,
                score = Random.nextDouble(0.85, 1.0).toFloat(),  // confianza simulada
                calories = caloriasPorAlimento[ingrediente] ?: 0
            )
        }
    }
}

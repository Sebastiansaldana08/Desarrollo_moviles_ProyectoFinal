package com.upch.proyectfinal.detection

data class FoodItem(
    val name: String,
    val score: Float,
    val boundingBox: List<Float>,
    val calories: Int
)


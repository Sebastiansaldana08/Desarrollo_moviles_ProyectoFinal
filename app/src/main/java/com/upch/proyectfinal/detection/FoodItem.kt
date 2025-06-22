package com.upch.proyectfinal.detection

data class FoodItem(
    val label: String,
    val score: Float,
    val boundingBox: List<Int> = emptyList(),
    val calories: Int = 0
)
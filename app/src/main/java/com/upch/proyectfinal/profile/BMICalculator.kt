package com.upch.proyectfinal.profile

data class BMIResult(
    val imc: Float,
    val category: String,
    val colorHex: String
)

fun calcularIMC(peso: Float, alturaCm: Float): BMIResult {
    val alturaM = alturaCm / 100
    if (alturaM <= 0f || peso <= 0f) {
        return BMIResult(0f, "Datos inválidos", "#9E9E9E")
    }

    val imc = peso / (alturaM * alturaM)
    val category = when {
        imc < 18.5 -> "Bajo peso"
        imc < 24.9 -> "Normal"
        imc < 29.9 -> "Sobrepeso"
        else -> "Obesidad"
    }
    val color = when (category) {
        "Bajo peso" -> "#03A9F4" // azul
        "Normal" -> "#4CAF50" // verde
        "Sobrepeso" -> "#FFC107" // ámbar
        "Obesidad" -> "#F44336" // rojo
        else -> "#9E9E9E"
    }

    return BMIResult(imc, category, color)
}

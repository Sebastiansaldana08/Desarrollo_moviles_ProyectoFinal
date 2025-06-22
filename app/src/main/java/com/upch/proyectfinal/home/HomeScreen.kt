package com.upch.proyectfinal.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a HealthSnapAI 🎉", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate("bmi") }) {
            Text("Ver IMC")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("camera") }) {
            Text("Escanear comida")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("history") }) {
            Text("Ver historial de comidas")
        }

    }
}
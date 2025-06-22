package com.upch.proyectfinal.detection

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.upch.proyectfinal.history.FoodLog
import com.upch.proyectfinal.history.FoodLogDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val uriEncoded = backStackEntry.arguments?.getString("imageUri")
    val imageUri = uriEncoded?.toUri()

    val context = LocalContext.current
    var foodItems by remember { mutableStateOf<List<FoodItem>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageUri) {
        imageUri?.let {
            try {
                val items = DetectionService.detectarAlimentos(context, it)
                foodItems = items

                if (items.isNotEmpty()) {
                    val foodLabel = items.joinToString(", ") { item -> item.label }
                    val calories = items.sumOf { it.calories }
                    val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

                    val foodLog = FoodLog(
                        foodLabel = foodLabel,
                        calories = calories,
                        dateTime = now
                    )

                    // Inserción segura en BD en un hilo IO
                    withContext(Dispatchers.IO) {
                        FoodLogDatabase.getInstance(context).foodLogDao().insert(foodLog)
                    }
                }
            } catch (e: Exception) {
                foodItems = emptyList()
                Log.e("SIMULATION_ERROR", "Fallo en simulación de alimentos", e)
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Resultado de detección") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        alignment = Alignment.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (foodItems.isNullOrEmpty()) {
                    Text("No se detectaron alimentos.")
                } else {
                    val totalCalories = foodItems!!.sumOf { it.calories }

                    Text("Ingredientes detectados:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(foodItems!!) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🍽 ${item.label}", style = MaterialTheme.typography.titleSmall)
                                    Text("Confianza simulada: ${(item.score * 100).toInt()}%")
                                    Text("Calorías estimadas: ${item.calories} kcal")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "🔢 Total de calorías: $totalCalories kcal",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                    Spacer(Modifier.width(8.dp))
                    Text("Volver al inicio")
                }
            }
        }
    }
}

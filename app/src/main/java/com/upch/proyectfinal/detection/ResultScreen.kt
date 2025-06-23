/*
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
// funcional
package com.upch.proyectfinal.detection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import androidx.compose.ui.draw.clip
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

    ResultContent(
        foodItems = foodItems,
        isLoading = isLoading,
        imageUriString = uriEncoded,
        onBackHome = { navController.navigate("home") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultContent(
    foodItems: List<FoodItem>?,
    isLoading: Boolean,
    imageUriString: String?,
    onBackHome: () -> Unit
) {
    val imageUri = imageUriString?.toUri()
    val totalCalories = foodItems?.sumOf { it.calories } ?: 0
    val dailyGoal = 1600
    val progress = totalCalories.toFloat() / dailyGoal

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resultado de detección") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    alignment = Alignment.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (foodItems.isNullOrEmpty()) {
                Text("No se detectaron alimentos.")
            } else {
                Text(
                    text = "Consumo de calorías",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Círculo de progreso
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    color = when {
                        progress >= 1f -> Color.Red
                        progress >= 0.75f -> Color(0xFFFFC107) // amarillo
                        else -> Color(0xFF4CAF50) // verde
                    },
                    trackColor = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$totalCalories kcal / $dailyGoal kcal",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Ingredientes detectados:", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(foodItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("🍽 ${item.label}", fontWeight = FontWeight.Bold)
                                Text("Confianza simulada: ${(item.score * 100).roundToInt()}%")
                                Text("Calorías estimadas: ${item.calories} kcal")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBackHome,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    val dummyItems = listOf(
        FoodItem("rice", 0.92f, listOf(), 130),
        FoodItem("fries potatoes", 0.96f, listOf(), 312),
        FoodItem("tomatoes", 0.88f, listOf(), 18),
        FoodItem("onion", 0.85f, listOf(), 40),
        FoodItem("meat", 0.97f, listOf(), 250)
    )

    ResultContent(
        foodItems = dummyItems,
        isLoading = false,
        imageUriString = null,
        onBackHome = {}
    )
}
*/
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import androidx.compose.ui.draw.clip
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush


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

    ResultContent(
        foodItems = foodItems,
        isLoading = isLoading,
        imageUriString = uriEncoded,
        onBackHome = { navController.navigate("home") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultContent(
    foodItems: List<FoodItem>?,
    isLoading: Boolean,
    imageUriString: String?,
    onBackHome: () -> Unit
) {
    val imageUri = imageUriString?.toUri()
    val totalCalories = foodItems?.sumOf { it.calories } ?: 0
    val dailyGoal = 1600
    val progress = totalCalories.toFloat() / dailyGoal

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "animatedProgress"
    )

    val progressColor = when {
        progress >= 0.8f -> Color.Red
        progress >= 0.5f -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }

    // Fondo degradado azul como en registro
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8BA9D6), // Azul claro
                        Color(0xFFCBD9F5)  // Más claro
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Resultado de detección") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    return@Column
                }

                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (foodItems.isNullOrEmpty()) {
                    Text("No se detectaron alimentos.")
                } else {
                    Text(
                        text = "Consumo de calorías",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        color = progressColor,
                        trackColor = Color(0xFFEDE7F6)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Progreso: ${(progress * 100).roundToInt()}%",
                        color = Color.Black, // Color fuerte
                        fontWeight = FontWeight.Bold, // Texto en negrita
                        fontSize = 16.sp, // Puedes subir a 18.sp si deseas
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "$totalCalories kcal / $dailyGoal kcal",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ingredientes detectados:", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(foodItems) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🍽 ${item.label}", fontWeight = FontWeight.Bold)
                                    Text("Confianza simulada: ${(item.score * 100).roundToInt()}%")
                                    Text("Calorías estimadas: ${item.calories} kcal")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBackHome,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F4EA0), // mismo azul que botón "Registrarse"
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Inicio")
                        Spacer(Modifier.width(8.dp))
                        Text("Volver al inicio")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    val dummyItems = listOf(
        FoodItem("rice", 0.92f, listOf(), 130),
        FoodItem("fries potatoes", 0.96f, listOf(), 312),
        FoodItem("tomatoes", 0.88f, listOf(), 18),
        FoodItem("onion", 0.85f, listOf(), 40),
        FoodItem("meat", 0.97f, listOf(), 250)
    )

    ResultContent(
        foodItems = dummyItems,
        isLoading = false,
        imageUriString = null,
        onBackHome = {}
    )
}


package com.upch.proyectfinal.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val userDao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    var bmiResult by remember { mutableStateOf<BMIResult?>(null) }
    var weightRange by remember { mutableStateOf<Pair<Float, Float>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // ───── Datos del usuario ─────
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userDao.getUser()?.let {
                bmiResult = calcularIMC(it.weight, it.height)
                val h = it.height / 100f
                weightRange = 18.5f * h * h to 24.9f * h * h
            }
        }
    }

    // Recomendar profesional si IMC ≥ 30
    LaunchedEffect(bmiResult) {
        if ((bmiResult?.imc ?: 0f) >= 30f) {
            snackbarHostState.showSnackbar("Te recomendamos consultar a un profesional de la salud")
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        topBar = {
            LargeTopAppBar(
                title = { Text("Tu IMC", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            bmiResult?.let { result ->
                DonutGauge(result, Modifier.size(240.dp))

                // Texto de peso ideal
                weightRange?.let { (min, max) ->
                    Text(
                        text = "Tu peso ideal está entre %.0f – %.0f kg".format(min, max),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                QuickTips(result.category)
            } ?: Text("No hay datos registrados", style = MaterialTheme.typography.bodyLarge)

            // Card desplegable explicativa
            ExpandableInfoCard()
        }
    }
}

// ───────────────────────── Donut Gauge ─────────────────────────
@Composable
private fun DonutGauge(result: BMIResult, modifier: Modifier = Modifier) {
    val sweepAngle = remember(result.imc) { (result.imc.coerceIn(0f, 50f) / 50f) * 270f }
    val bgArcColor = Color.White.copy(alpha = 0.25f)
    val fgArcColor = Color.White
    val strokeW = 22.dp

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor(result.colorHex))),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = bgArcColor,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeW.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = fgArcColor,
                    startAngle = 135f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeW.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(44.dp))
                Text("%.2f".format(result.imc), style = MaterialTheme.typography.displaySmall.copy(fontSize = 34.sp), color = Color.White)
                Text(result.category, style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
    }
}

// ───────────────────────── Quick Tips ─────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickTips(category: String) {
    val tips = when (category) {
        "Bajo peso" -> listOf("Aumenta calorías con snacks saludables", "Incorpora proteína en cada comida")
        "Normal" -> listOf("Mantén tu actividad física regular", "Equilibra macronutrientes")
        "Sobrepeso" -> listOf("Controla porciones y azúcares añadidos", "Añade 30 min de cardio diario")
        else -> listOf("Consulta a un nutricionista", "Incrementa actividad aeróbica", "Reduce alimentos ultraprocesados")
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tips.forEach { tip ->
            AssistChip(onClick = {}, shape = RoundedCornerShape(50), label = { Text(tip, textAlign = TextAlign.Center) })
        }
    }
}

// ───────────────────────── Info Card ─────────────────────────
@Composable
private fun ExpandableInfoCard() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("¿Qué es el IMC?", style = MaterialTheme.typography.titleLarge)

            AnimatedVisibility(expanded, enter = fadeIn(tween(300)), exit = fadeOut(tween(200))) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "El Índice de Masa Corporal (IMC) se calcula dividiendo el peso (kg) entre la altura (m) al cuadrado. Ayuda a clasificar el peso corporal:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text("• <18.5: Bajo peso", style = MaterialTheme.typography.bodyMedium)
                    Text("• 18.5–24.9: Normal", style = MaterialTheme.typography.bodyMedium)
                    Text("• 25–29.9: Sobrepeso", style = MaterialTheme.typography.bodyMedium)
                    Text("• ≥30: Obesidad", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Text(
                if (expanded) "Toca para contraer" else "Toca para expandir",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

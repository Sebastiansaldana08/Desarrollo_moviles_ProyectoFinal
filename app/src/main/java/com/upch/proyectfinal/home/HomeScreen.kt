/*
package com.upch.proyectfinal.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upch.proyectfinal.profile.BMIResult
import com.upch.proyectfinal.profile.calcularIMC

// =============================================================================================
//  SIMPLE MODELS (replace later by your repository / database layer)
// =============================================================================================

data class User(val weight: Float, val height: Float, val name: String?)

fun tipsFor(category: String): List<String> = when (category) {
    "Bajo peso"   -> listOf("Aumenta ingesta calórica saludable", "Incluye proteínas en cada comida")
    "Normal"      -> listOf("Mantén tu rutina", "Monitorea tu progreso")
    "Sobrepeso"   -> listOf("Incrementa actividad aeróbica", "Controla porciones diarias")
    else           -> listOf("Consulta a un nutricionista", "Reduce ultraprocesados", "Haz ejercicio aeróbico")
}

// =============================================================================================
//  HOME SCREEN  – pure UI; the *only* logic reused es calcularIMC() ya existente.
// =============================================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, user: User? = null) {
    // Reutilizamos la lógica existente para IMC
    val bmiResult = user?.let { calcularIMC(it.weight, it.height) }

    val gradient = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.10f),
            MaterialTheme.colorScheme.surface
        ),
        radius = 800f
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("HealthSnapAI", style = MaterialTheme.typography.headlineLarge)
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(350)),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .systemBarsPadding()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                bmiResult?.let { MiniDashboard(it) }

                GreetingSection(user?.name)

                DailySummaryCard()

                QuickAccessRow(navController)

                bmiResult?.let { TipsSection(it.category) }

                InspirationalQuoteChip()

                ChallengeCard()

                // Placeholder illustration; reemplázala por tu recurso
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 160.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                FooterNav()
            }
        }
    }
}

// =============================================================================================
//  UI SECTIONS
// =============================================================================================

@Composable
private fun DailySummaryCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Resumen diario", style = MaterialTheme.typography.titleMedium)
            Divider()
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Calorías", value = "— kcal")
                SummaryItem(label = "Agua", value = "— L")
                SummaryItem(label = "Sueño", value = "— h")
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ChallengeCard() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Reto rápido", style = MaterialTheme.typography.titleMedium)
                Text("Camina 6 000 pasos hoy 📶", style = MaterialTheme.typography.bodyMedium)
            }
            FilledTonalButton(onClick = { /* TODO: Implement accept */ }) {
                Text("¡Acepto!")
            }
        }
    }
}

@Composable
private fun InspirationalQuoteChip() {
    AssistChip(
        onClick = { },
        label = { Text("“El progreso, no la perfección, es la meta” 🍀") },
        shape = RoundedCornerShape(50),
        colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    )
}

@Composable
private fun FooterNav() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Ajustes")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Info, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Acerca")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Support, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Soporte")
        }
    }
}

@Composable
private fun MiniDashboard(result: BMIResult) {
    val ringColor = remember(result.colorHex) {
        Color(android.graphics.Color.parseColor(result.colorHex))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BmiMiniGauge(result, ringColor, Modifier.size(140.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Tu IMC", style = MaterialTheme.typography.titleMedium)
                Text(
                    String.format("%.2f", result.imc),
                    style = MaterialTheme.typography.headlineSmall,
                    color = ringColor
                )
                Text(result.category, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                StatChip(Icons.Outlined.LocalFireDepartment, "1 350 kcal")
                Spacer(Modifier.height(6.dp))
                StatChip(Icons.Outlined.DirectionsRun, "5 200 pasos")
            }
        }
    }
}

@Composable
private fun BmiMiniGauge(result: BMIResult, ringColor: Color, modifier: Modifier = Modifier) {
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val progress = (result.imc / 50f).coerceIn(0f, 1f)

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Icon(Icons.Default.Favorite, contentDescription = null, tint = ringColor)
    }
}

@Composable
private fun StatChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    AssistChip(
        onClick = { },
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        shape = CircleShape
    )
}

@Composable
private fun GreetingSection(name: String?) {
    val greetings = listOf(
        "¡Mantente motivado hoy!",
        "Cada paso cuenta.",
        "¡Sigue cuidando tu salud!",
        "Una comida equilibrada te acerca a tu meta."
    )
    val phrase = remember { greetings.random() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¡Hola${if (!name.isNullOrBlank()) ", $name" else ""}! \uD83C\uDF4F",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = phrase,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickAccessRow(navController: NavController) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        QuickChip(Icons.Default.Favorite, "IMC") { navController.navigate("bmi") }
        QuickChip(Icons.Default.CameraAlt, "Escanear") { navController.navigate("camera") }
        QuickChip(Icons.Default.History, "Historial") { navController.navigate("history") }
    }
}

@Composable
private fun QuickChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        shape = RoundedCornerShape(50)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TipsSection(category: String) {
    val tips = tipsFor(category)
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Tips de hoy", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tips.forEach { tip ->
                AssistChip(
                    onClick = { },
                    label = { Text(tip) },
                    shape = RoundedCornerShape(50)
                )
            }
        }
    }
}
*/
package com.upch.proyectfinal.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upch.proyectfinal.profile.BMIResult
import com.upch.proyectfinal.profile.calcularIMC
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.upch.proyectfinal.ui.theme.HealthSnapTheme


// =============================================================================================
//  SIMPLE MODELS (replace later by your repository / database layer)
// =============================================================================================

data class User(val weight: Float, val height: Float, val name: String?)

fun tipsFor(category: String): List<String> = when (category) {
    "Bajo peso"   -> listOf("Aumenta ingesta calórica saludable", "Incluye proteínas en cada comida")
    "Normal"      -> listOf("Mantén tu rutina", "Monitorea tu progreso")
    "Sobrepeso"   -> listOf("Incrementa actividad aeróbica", "Controla porciones diarias")
    else           -> listOf("Consulta a un nutricionista", "Reduce ultraprocesados", "Haz ejercicio aeróbico")
}

// =============================================================================================
//  HOME SCREEN  – pure UI; the *only* logic reused es calcularIMC() ya existente.
// =============================================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, user: User? = null) {
    // Reutilizamos la lógica existente para IMC
    val bmiResult = user?.let { calcularIMC(it.weight, it.height) }

    val gradient = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.10f),
            MaterialTheme.colorScheme.surface
        ),
        radius = 800f
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("HealthSnapAI", style = MaterialTheme.typography.headlineLarge)
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(350)),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .systemBarsPadding()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                bmiResult?.let { MiniDashboard(it) }

                GreetingSection(user?.name)

                DailySummaryCard()

                QuickAccessRow(navController)

                bmiResult?.let { TipsSection(it.category) }

                InspirationalQuoteChip()

                ChallengeCard()

                // Placeholder illustration; reemplázala por tu recurso
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 160.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                FooterNav()
            }
        }
    }
}

// =============================================================================================
//  UI SECTIONS
// =============================================================================================

@Composable
private fun DailySummaryCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Resumen diario", style = MaterialTheme.typography.titleMedium)
            Divider()
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Calorías", value = "— kcal")
                SummaryItem(label = "Agua", value = "— L")
                SummaryItem(label = "Sueño", value = "— h")
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ChallengeCard() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Reto rápido", style = MaterialTheme.typography.titleMedium)
                Text("Camina 6 000 pasos hoy 📶", style = MaterialTheme.typography.bodyMedium)
            }
            FilledTonalButton(onClick = { /* TODO: Implement accept */ }) {
                Text("¡Acepto!")
            }
        }
    }
}

@Composable
private fun InspirationalQuoteChip() {
    AssistChip(
        onClick = { },
        label = { Text("“El progreso, no la perfección, es la meta” 🍀") },
        shape = RoundedCornerShape(50),
        colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    )
}

@Composable
private fun FooterNav() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Ajustes")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Info, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Acerca")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Support, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Soporte")
        }
    }
}

@Composable
private fun MiniDashboard(result: BMIResult) {
    val ringColor = remember(result.colorHex) {
        Color(android.graphics.Color.parseColor(result.colorHex))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BmiMiniGauge(result, ringColor, Modifier.size(140.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Tu IMC", style = MaterialTheme.typography.titleMedium)
                Text(
                    String.format("%.2f", result.imc),
                    style = MaterialTheme.typography.headlineSmall,
                    color = ringColor
                )
                Text(result.category, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                StatChip(Icons.Outlined.LocalFireDepartment, "1 350 kcal")
                Spacer(Modifier.height(6.dp))
                StatChip(Icons.Outlined.DirectionsRun, "5 200 pasos")
            }
        }
    }
}

@Composable
private fun BmiMiniGauge(result: BMIResult, ringColor: Color, modifier: Modifier = Modifier) {
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val progress = (result.imc / 50f).coerceIn(0f, 1f)

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Icon(Icons.Default.Favorite, contentDescription = null, tint = ringColor)
    }
}

@Composable
private fun StatChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    AssistChip(
        onClick = { },
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        shape = CircleShape
    )
}

@Composable
private fun GreetingSection(name: String?) {
    val greetings = listOf(
        "¡Mantente motivado hoy!",
        "Cada paso cuenta.",
        "¡Sigue cuidando tu salud!",
        "Una comida equilibrada te acerca a tu meta."
    )
    val phrase = remember { greetings.random() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¡Hola${if (!name.isNullOrBlank()) ", $name" else ""}! \uD83C\uDF4F",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = phrase,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickAccessRow(navController: NavController) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        QuickChip(Icons.Default.Favorite, "IMC") { navController.navigate("bmi") }
        QuickChip(Icons.Default.CameraAlt, "Escanear") { navController.navigate("camera") }
        QuickChip(Icons.Default.History, "Historial") { navController.navigate("history") }
    }
}

@Composable
private fun QuickChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        shape = RoundedCornerShape(50)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TipsSection(category: String) {
    val tips = tipsFor(category)
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Tips de hoy", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tips.forEach { tip ->
                AssistChip(
                    onClick = { },
                    label = { Text(tip) },
                    shape = RoundedCornerShape(50)
                )
            }
        }
    }
}
// Función auxiliar para simular el usuario
private val fakeUser = User(
    weight = 68f,     // en kg
    height = 1.72f,   // en metros
    name = "Renzo"
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    // Envolvemos todo en el tema para respetar los estilos visuales
    HealthSnapTheme {
        // Usamos rememberNavController() aunque no navegue realmente en el preview
        HomeScreen(navController = rememberNavController(), user = fakeUser)
    }
}

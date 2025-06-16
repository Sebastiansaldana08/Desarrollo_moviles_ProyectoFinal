package com.upch.proyectfinal.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun BMIScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val userDao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    var bmiResult by remember { mutableStateOf<BMIResult?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val user = userDao.getUser()
            user?.let {
                bmiResult = calcularIMC(it.weight, it.height)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado de IMC", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        bmiResult?.let { result ->
            Box(
                modifier = Modifier
                    .background(Color(android.graphics.Color.parseColor(result.colorHex)))
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("IMC: %.2f".format(result.imc), style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text(result.category, style = MaterialTheme.typography.titleMedium, color = Color.White, textAlign = TextAlign.Center)
                }
            }
        } ?: run {
            Text("No hay datos registrados")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("home") }) {
            Text("Volver al inicio")
        }
    }
}

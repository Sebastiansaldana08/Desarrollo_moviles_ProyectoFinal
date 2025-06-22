package com.upch.proyectfinal.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    var logs by remember { mutableStateOf<List<FoodLog>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar historial al iniciar
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val dao = FoodLogDatabase.getInstance(context).foodLogDao()
            logs = dao.getAll().first()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de comidas") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar historial")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (logs.isEmpty()) {
                Text("Aún no hay registros guardados.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(logs) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("🕒 ${log.dateTime}", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("🍽 ${log.foodLabel}", style = MaterialTheme.typography.bodyLarge)
                                Text("🔥 ${log.calories} kcal", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
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

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("¿Eliminar historial?") },
                text = { Text("Esta acción eliminará todos los registros de comidas guardados.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                val dao = FoodLogDatabase.getInstance(context).foodLogDao()
                                dao.deleteAll()
                                val updatedLogs = dao.getAll().first()
                                withContext(Dispatchers.Main) {
                                    logs = updatedLogs
                                }
                            }
                        }
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

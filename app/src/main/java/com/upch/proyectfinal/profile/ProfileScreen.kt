package com.upch.proyectfinal.profile

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val userDao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    var gender by remember { mutableStateOf<String?>(null) }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Datos Personales", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuBox(
            label = "Sexo",
            options = listOf("Masculino", "Femenino", "Otro"),
            selected = gender,
            onSelected = { gender = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Edad") })
        OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Altura (cm)") })
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (kg)") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                if (gender.isNullOrEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                    // Muestra un mensaje de error (temporal)
                    return@launch
                }

                userDao.insertUser(
                    UserData(
                        gender = gender!!,
                        age = age.toIntOrNull() ?: 0,
                        height = height.toFloatOrNull() ?: 0f,
                        weight = weight.toFloatOrNull() ?: 0f
                    )
                )

                navController.navigate("home") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }) {
            Text("Guardar")
        }
    }
}

@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = label)
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selected ?: "Seleccione su género")
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


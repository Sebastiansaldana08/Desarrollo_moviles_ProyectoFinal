package com.upch.proyectfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.upch.proyectfinal.auth.LoginScreen
import com.upch.proyectfinal.auth.RegisterScreen
import com.upch.proyectfinal.home.HomeScreen
import com.upch.proyectfinal.profile.ProfileScreen // <-- Importa esta pantalla

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile") { ProfileScreen(navController) } // <-- Añadido
        composable("home") { HomeScreen() }
    }
}

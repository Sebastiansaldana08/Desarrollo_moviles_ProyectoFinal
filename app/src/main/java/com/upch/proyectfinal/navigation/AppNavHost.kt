package com.upch.proyectfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.upch.proyectfinal.auth.LoginScreen
import com.upch.proyectfinal.auth.RegisterScreen
import com.upch.proyectfinal.home.HomeScreen
import com.upch.proyectfinal.profile.ProfileScreen // <-- Importa esta pantalla
import com.upch.proyectfinal.profile.BMIScreen
import com.upch.proyectfinal.detection.CameraScreen
import com.upch.proyectfinal.detection.ResultScreen
import androidx.navigation.navArgument
import com.upch.proyectfinal.history.HistoryScreen

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("bmi") { BMIScreen(navController) }
        composable("camera") { CameraScreen(navController) }
        composable(
            route = "result?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") {})
        ) { backStackEntry ->
            ResultScreen(navController, backStackEntry)
        }
        composable("history") {
            HistoryScreen(navController)
        }
    }
}
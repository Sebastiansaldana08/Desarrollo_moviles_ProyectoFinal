package com.upch.proyectfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.upch.proyectfinal.navigation.AppNavHost
import com.upch.proyectfinal.ui.theme.HealthSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthSnapTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

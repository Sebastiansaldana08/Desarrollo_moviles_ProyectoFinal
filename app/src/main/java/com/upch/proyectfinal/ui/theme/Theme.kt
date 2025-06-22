package com.upch.proyectfinal.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// -----------------------------------------------------------------------------
//  1. COLOR SCHEMES – Vibrant Vitality palette (see Color.kt)
// -----------------------------------------------------------------------------

private val LightColors = lightColorScheme(
    primary            = BluePrimary,
    onPrimary          = BlueOnPrimary,
    primaryContainer   = BlueContainer,
    onPrimaryContainer = BlueOnContainer,

    secondary            = TurquoiseSecondary,
    onSecondary          = BlueOnPrimary,
    secondaryContainer   = TurquoiseContainer,
    onSecondaryContainer = BlueOnContainer,

    tertiary            = CoralTertiary,
    onTertiary          = BlueOnPrimary,
    tertiaryContainer   = CoralContainer,
    onTertiaryContainer = BlueOnContainer,

    background = Color(0xFFFDFEFF),
    surface    = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1B1B1F),
    onSurface    = Color(0xFF1B1B1F)
)

private val DarkColors = darkColorScheme(
    primary            = BlueContainer,
    onPrimary          = BlueOnContainer,
    primaryContainer   = BluePrimary,
    onPrimaryContainer = BlueOnPrimary,

    secondary            = TurquoiseContainer,
    onSecondary          = BlueOnContainer,
    secondaryContainer   = TurquoiseSecondary,
    onSecondaryContainer = BlueOnPrimary,

    tertiary            = CoralContainer,
    onTertiary          = BlueOnContainer,
    tertiaryContainer   = CoralTertiary,
    onTertiaryContainer = BlueOnPrimary,

    background = Color(0xFF101113),
    surface    = Color(0xFF181A1D),
    onBackground = Color(0xFFE3E3E7),
    onSurface    = Color(0xFFE3E3E7)
)

// -----------------------------------------------------------------------------
//  2. THEME COMPOSABLE
// -----------------------------------------------------------------------------

@Composable
fun HealthSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else      -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,  // defined in Type.kt
        content     = content
    )
}

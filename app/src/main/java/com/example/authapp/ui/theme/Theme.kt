package com.example.authapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3B5BFE),
    secondary = Color(0xFF6E7CF6),
    background = Color(0xFFFAFAFF),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun AuthAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}

package com.talhaoz.biriktir.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyAppTheme(theme: AppTheme, content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = theme.primary,
        onPrimary = Color.White,
        background = Color(0xFFF6F6F6),
        surface = Color.White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun AlaminGlobalEarnTheme(
    darkModeOption: String = "system", // "light", "dark", "system"
    accentIndex: Int = 0, // 0: Navy/Blue, 1: Emerald Green, 2: Royal Purple, 3: Sunset Amber
    content: @Composable () -> Unit
) {
    val isDark = when (darkModeOption) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val primaryColor = when (accentIndex) {
        1 -> GreenSuccess
        2 -> PurpleAccent
        3 -> AmberAccent
        else -> BlueAccent
    }

    val primaryContainer = when (accentIndex) {
        1 -> Color(0xFFD4EDDA)
        2 -> Color(0xFFE8E5FA)
        3 -> Color(0xFFFFECD6)
        else -> Color(0xFFD9EEFF)
    }

    val darkPrimaryContainer = when (accentIndex) {
        1 -> Color(0xFF0F4D37)
        2 -> Color(0xFF38297A)
        3 -> Color(0xFF632B09)
        else -> Color(0xFF0F3E68)
    }

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = darkPrimaryContainer,
            onPrimaryContainer = Color.White,
            secondary = GreenSuccess,
            onSecondary = Color.White,
            background = BackgroundDark,
            onBackground = TextPrimaryDark,
            surface = SurfaceDark,
            onSurface = TextPrimaryDark,
            surfaceVariant = SurfaceVariantDark,
            onSurfaceVariant = TextSecondaryDark,
            outline = DividerDark
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryContainer,
            onPrimaryContainer = NavyDeep,
            secondary = GreenSuccess,
            onSecondary = Color.White,
            background = BackgroundLight,
            onBackground = TextPrimaryLight,
            surface = SurfaceLight,
            onSurface = TextPrimaryLight,
            surfaceVariant = Color(0xFFEEF3F8),
            onSurfaceVariant = TextSecondaryLight,
            outline = DividerLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

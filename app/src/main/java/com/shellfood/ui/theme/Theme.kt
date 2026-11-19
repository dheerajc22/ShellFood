package com.shellfood.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val ShellLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF6B00),
    onPrimary = Color.White,

    secondary = Color(0xFFFFA45C),
    onSecondary = Color(0xFF252525),

    tertiary = Color(0xFFFFC078),
    onTertiary = Color(0xFF252525),

    background = Color(0xFFFFF7F0),
    onBackground = Color(0xFF252525),

    surface = Color.White,
    onSurface = Color(0xFF252525)
)
private val ShellDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF914D),
    onPrimary = Color(0xFF252525),

    secondary = Color(0xFFFFB477),
    onSecondary = Color(0xFF252525),

    tertiary = Color(0xFFFFC078),
    onTertiary = Color(0xFF252525),

    background = Color(0xFF252525),
    onBackground = Color.White,

    surface = Color(0xFF333333),
    onSurface = Color.White
)
@Composable
fun ShellFoodTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        ShellDarkColorScheme
    } else {
        ShellLightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

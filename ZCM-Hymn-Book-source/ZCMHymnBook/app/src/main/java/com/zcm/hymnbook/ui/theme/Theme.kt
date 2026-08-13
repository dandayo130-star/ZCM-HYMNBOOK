package com.zcm.hymnbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.zcm.hymnbook.data.preferences.AppTheme

private val LightColors = lightColorScheme(
    primary = BurgundyPrimaryLight,
    onPrimary = Color.White,
    secondary = GoldSecondaryLight,
    onSecondary = Color.White,
    background = IvoryBackgroundLight,
    onBackground = OnLight,
    surface = IvorySurfaceLight,
    onSurface = OnLight,
    surfaceVariant = IvorySurfaceVariantLight,
    onSurfaceVariant = OnLight,
    error = ErrorLight
)

private val DarkColors = darkColorScheme(
    primary = BurgundyPrimaryDark,
    onPrimary = OnDark,
    secondary = GoldSecondaryDark,
    onSecondary = CharcoalBackgroundDark,
    background = CharcoalBackgroundDark,
    onBackground = OnDark,
    surface = CharcoalSurfaceDark,
    onSurface = OnDark,
    surfaceVariant = CharcoalSurfaceVariantDark,
    onSurfaceVariant = OnDark,
    error = ErrorDark
)

@Composable
fun ZcmHymnBookTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

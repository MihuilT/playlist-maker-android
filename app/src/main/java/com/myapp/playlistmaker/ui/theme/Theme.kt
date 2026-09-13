package com.myapp.playlistmaker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

// Material3 ColorScheme нужен, чтобы дефолтные компоненты (Text, Icon, Card и т.п.)
// автоматически получали корректные цвета для светлой/тёмной темы.
// Раньше здесь была "фиолетовая" палитра-шаблон, из-за чего часть текста/иконок
// могла оставаться тёмной даже при включённой тёмной теме.
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    secondary = TextSecondaryDark,
    onSecondary = TextPrimaryDark,
    tertiary = PrimaryBlue
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    background = BackgroundGray,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    secondary = TextSecondary,
    onSecondary = TextPrimary,
    tertiary = PrimaryBlue
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Динамические цвета могут конфликтовать с нашей кастомной палитрой.
    // По умолчанию выключаем, чтобы тема выглядела предсказуемо на всех устройствах.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val appColors = if (darkTheme) {
        PlaylistMakerColors(
            primary = PrimaryBlue,
            surface = SurfaceDark,
            background = BackgroundDark,
            textPrimary = TextPrimaryDark,
            textSecondary = TextSecondaryDark,
            switchTrackEnabled = SwitchTrackEnabled,
            switchTrackDisabled = SwitchTrackDisabledDark,
            switchThumbEnabled = SwitchThumbEnabled,
            switchThumbDisabled = SwitchThumbDisabledDark
        )
    } else {
        PlaylistMakerColors(
            primary = PrimaryBlue,
            surface = SurfaceWhite,
            background = BackgroundGray,
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            switchTrackEnabled = SwitchTrackEnabled,
            switchTrackDisabled = SwitchTrackDisabled,
            switchThumbEnabled = SwitchThumbEnabled,
            switchThumbDisabled = SwitchThumbDisabled
        )
    }

    CompositionLocalProvider(LocalPlaylistMakerColors provides appColors) {
        MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
    }
}
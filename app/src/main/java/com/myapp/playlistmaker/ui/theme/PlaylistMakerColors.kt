package com.myapp.playlistmaker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class PlaylistMakerColors(
    val primary: Color,
    val surface: Color,
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val switchTrackEnabled: Color,
    val switchTrackDisabled: Color,
    val switchThumbEnabled: Color,
    val switchThumbDisabled: Color
)

val LocalPlaylistMakerColors = staticCompositionLocalOf {
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

object PlaylistMakerAppTheme {
    val colors: PlaylistMakerColors
        @Composable get() = LocalPlaylistMakerColors.current
}

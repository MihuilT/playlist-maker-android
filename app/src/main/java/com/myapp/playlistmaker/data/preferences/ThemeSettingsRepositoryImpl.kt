package com.myapp.playlistmaker.data.preferences

import com.myapp.playlistmaker.domain.ThemeSettingsRepository
import kotlinx.coroutines.flow.Flow

class ThemeSettingsRepositoryImpl(
    private val themeSettingsStorage: ThemeSettingsStorage
) : ThemeSettingsRepository {

    override fun observeDarkThemeEnabled(): Flow<Boolean> =
        themeSettingsStorage.observeDarkThemeEnabled()

    override suspend fun setDarkThemeEnabled(isEnabled: Boolean) {
        themeSettingsStorage.setDarkThemeEnabled(isEnabled)
    }
}

package com.myapp.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface ThemeSettingsRepository {
    fun observeDarkThemeEnabled(): Flow<Boolean>
    suspend fun setDarkThemeEnabled(isEnabled: Boolean)
}

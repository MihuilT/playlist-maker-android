package com.myapp.playlistmaker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeSettingsStorage(
    private val dataStore: DataStore<Preferences>
) {
    fun observeDarkThemeEnabled(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[KEY_DARK_THEME_ENABLED] ?: false
        }

    suspend fun setDarkThemeEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_DARK_THEME_ENABLED] = isEnabled
        }
    }

    private companion object {
        val KEY_DARK_THEME_ENABLED = booleanPreferencesKey("dark_theme_enabled")
    }
}

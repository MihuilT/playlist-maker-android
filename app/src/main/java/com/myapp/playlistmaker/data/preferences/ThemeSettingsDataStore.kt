package com.myapp.playlistmaker.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.themeSettingsDataStore by preferencesDataStore(
    name = "theme_settings_datastore"
)

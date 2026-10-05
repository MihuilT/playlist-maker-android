package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.ThemeSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themeSettingsRepository: ThemeSettingsRepository
) : ViewModel() {

    val isDarkThemeEnabled: StateFlow<Boolean> =
        themeSettingsRepository.observeDarkThemeEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setDarkThemeEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            themeSettingsRepository.setDarkThemeEnabled(isEnabled)
        }
    }
}

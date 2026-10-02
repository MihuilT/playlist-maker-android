package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.PlaylistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _coverImageUri = MutableStateFlow<String?>(null)

    val coverImageUri: StateFlow<String?> = _coverImageUri.asStateFlow()

    fun setCoverImageUri(uri: String?) {
        _coverImageUri.value = uri
    }

    fun createPlaylist(name: String, description: String, coverImageUri: String? = null) {
        viewModelScope.launch {
            playlistRepository.addNewPlaylist(name, description, coverImageUri)
            _coverImageUri.value = null
        }
    }
}
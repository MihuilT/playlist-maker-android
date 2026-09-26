package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.PlaylistRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _playlistList = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlistList.asStateFlow()

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collect { playlists ->
                _playlistList.value = playlists
            }
        }
    }
}
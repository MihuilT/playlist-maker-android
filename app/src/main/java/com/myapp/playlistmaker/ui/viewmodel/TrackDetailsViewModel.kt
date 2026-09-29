package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.PlaylistRepository
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.domain.TrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackDetailsViewModel(
    private val track: Track,
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    sealed class TrackDetailsUiState {
        data class Content(val track: Track, val playlists: List<Playlist>) : TrackDetailsUiState()
        object Loading : TrackDetailsUiState()
        data class Error(val message: String) : TrackDetailsUiState()
    }

    private val _uiState = MutableStateFlow<TrackDetailsUiState>(TrackDetailsUiState.Content(track, emptyList()))
    val uiState: StateFlow<TrackDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            trackRepository.getTrackByNameAndArtist(track).collectLatest { updatedTrack ->
                val currentPlaylists = (_uiState.value as TrackDetailsUiState.Content).playlists
                if (updatedTrack != null) {
                    _uiState.value = TrackDetailsUiState.Content(updatedTrack, currentPlaylists)
                }
            }
        }

        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collectLatest { playlists ->
                val currentTrack = (_uiState.value as TrackDetailsUiState.Content).track
                _uiState.value = TrackDetailsUiState.Content(currentTrack, playlists)
            }
        }
    }

    fun toggleFavorite() {
        val currentTrack = (_uiState.value as? TrackDetailsUiState.Content)?.track ?: return

        val newFavoriteStatus = !currentTrack.favorite

        viewModelScope.launch {
            trackRepository.updateTrackFavoriteStatus(currentTrack, newFavoriteStatus)
        }
    }

    fun addToPlaylist(playlistId: Long) {
        val currentTrack = (_uiState.value as? TrackDetailsUiState.Content)?.track ?: return

        viewModelScope.launch {
            trackRepository.insertSongToPlaylist(
                currentTrack.copy(playlistId = playlistId),
                playlistId
            )
        }
    }
}
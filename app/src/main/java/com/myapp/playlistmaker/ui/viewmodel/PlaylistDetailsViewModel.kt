package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.PlaylistRepository
import com.myapp.playlistmaker.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistId: Long,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _playlistDetails = MutableStateFlow<Playlist?>(null)
    val playlistDetails: StateFlow<Playlist?> = _playlistDetails.asStateFlow()

    private val _playlistDeleted = MutableStateFlow(false)
    val playlistDeleted: StateFlow<Boolean> = _playlistDeleted.asStateFlow()

    private val _isLoading = MutableStateFlow(true)

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            _isLoading.value = true
            playlistRepository.getPlaylist(playlistId).collect { playlist ->
                _playlistDetails.value = playlist
                _isLoading.value = false
            }
        }
    }

    fun removeTrack(track: Track) {
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(track)
        }
    }

    fun totalMinutes(tracks: List<Track>): Int {
        return tracks.sumOf { track ->
            track.trackTime
                .replace(":", ".")
                .split(".")
                .let { timeParts -> timeParts[0].toInt() * 60 + timeParts[1].toInt() }
        } / 60
    }
    fun deletePlaylist() {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId)
            _playlistDeleted.value = true
        }
    }

}
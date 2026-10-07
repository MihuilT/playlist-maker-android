package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.domain.TrackRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    private val trackRepository: TrackRepository
) : ViewModel() {

    // текст поиска по своей коллекции
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // пустой запрос - все избранные треки, иначе поиск через полнотекстовый индекс
    val favoriteTracks: StateFlow<List<Track>> = _query
        .flatMapLatest { text ->
            if (text.isBlank()) {
                trackRepository.getFavoriteTracks()
            } else {
                trackRepository.searchInCollection(text).map { tracks -> tracks.filter { it.favorite } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(text: String) {
        _query.value = text
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch {
            val updatedTrack = track.copy(favorite = isFavorite)
            trackRepository.updateTrackFavoriteStatus(updatedTrack, isFavorite)
            trackRepository.deleteIfNotFavoriteAndNotInPlaylist(updatedTrack)
        }
    }
}

package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.GeneratePlaylistUseCase
import com.myapp.playlistmaker.domain.Genre
import com.myapp.playlistmaker.domain.Mood
import com.myapp.playlistmaker.domain.PlaylistRepository
import com.myapp.playlistmaker.domain.PlaylistRequest
import com.myapp.playlistmaker.domain.ProfileRepository
import com.myapp.playlistmaker.domain.SortRule
import com.myapp.playlistmaker.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class GeneratePlaylistUiState(
    val genres: Set<Genre> = emptySet(),
    val mood: Mood = Mood.ENERGETIC,
    val size: Int = 20,
    val sortRule: SortRule = SortRule.DEFAULT,
    val isLoading: Boolean = false,
    val tracks: List<Track>? = null,   // null - подбор еще не запускался
    val isError: Boolean = false,
    val isSaved: Boolean = false
)

class GeneratePlaylistViewModel(
    private val generatePlaylist: GeneratePlaylistUseCase,
    private val playlistRepository: PlaylistRepository,
    profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GeneratePlaylistUiState())
    val state: StateFlow<GeneratePlaylistUiState> = _state.asStateFlow()

    init {
        // любимые жанры и настроение по умолчанию берем из профиля
        viewModelScope.launch {
            val profile = profileRepository.observeProfile().first()
            _state.update { it.copy(genres = profile.favoriteGenres, mood = profile.defaultMood) }
        }
    }

    fun toggleGenre(genre: Genre) {
        _state.update { state ->
            val genres = if (genre in state.genres) state.genres - genre else state.genres + genre
            state.copy(genres = genres)
        }
    }

    fun onMoodChange(mood: Mood) {
        _state.update { it.copy(mood = mood) }
    }

    fun onSizeChange(size: Int) {
        _state.update { it.copy(size = size) }
    }

    fun onSortChange(rule: SortRule) {
        _state.update { state ->
            state.copy(sortRule = rule, tracks = state.tracks?.sortedWith(rule.comparator))
        }
    }

    fun generate() {
        val current = _state.value
        if (current.genres.isEmpty() || current.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                val tracks = generatePlaylist(
                    PlaylistRequest(
                        genres = current.genres,
                        mood = current.mood,
                        size = current.size,
                        sortRule = current.sortRule
                    )
                )
                _state.update { it.copy(isLoading = false, tracks = tracks) }
            } catch (_: IOException) {
                _state.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun save(description: String) {
        val current = _state.value
        val tracks = current.tracks
        if (tracks.isNullOrEmpty()) return

        viewModelScope.launch {
            val name = current.mood.title + ": " + current.genres.joinToString(", ") { it.apiName }
            playlistRepository.createPlaylistWithTracks(name, description, tracks)
            _state.update { it.copy(isSaved = true) }
        }
    }
}

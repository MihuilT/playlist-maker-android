package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.SearchHistoryRepository
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.domain.TrackRepository
import com.myapp.playlistmaker.domain.SearchQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class)
class SearchViewModel(
    private val trackRepository: TrackRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _historyQueries = MutableStateFlow<List<SearchQuery>>(emptyList())

    private val _searchQuery = MutableStateFlow("")

    init {
        observeHistory()

        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isEmpty()) {
                        clearSearch()
                    } else {
                        // поиск во время набора: промежуточный текст в историю не попадает
                        search(query, addToHistory = false)
                    }
                }
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            searchHistoryRepository.observeHistory().collect { historyList ->
                _historyQueries.value = historyList
            }
        }
    }

    fun onQueryChange(newValue: String) {
        _searchQuery.value = newValue
    }

    // addToHistory = true, когда поиск запустил сам пользователь (лупа или запрос из истории)
    fun search(query: String, addToHistory: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = SearchUiState.Searching

                if (addToHistory) {
                    searchHistoryRepository.saveQuery(SearchQuery(query = query))
                }

                val tracks = trackRepository.searchTracks(query = query)
                _uiState.value = SearchUiState.Success(tracks = tracks)

            } catch (e: IOException) {
                _uiState.value = SearchUiState.Fail(e.message.toString())
            }
        }
    }

    // запрос сохраняется в историю, когда пользователь открыл найденный трек
    fun saveQueryToHistory(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            searchHistoryRepository.saveQuery(SearchQuery(query = query))
        }
    }

    fun clearSearch() {
        _uiState.value = SearchUiState.Initial
    }

    suspend fun loadHistory(): List<SearchQuery> {
        return searchHistoryRepository.observeHistory().first()
    }
}

sealed class SearchUiState {
    object Initial : SearchUiState()
    object Searching : SearchUiState()
    data class Success(val tracks: List<Track>) : SearchUiState()
    data class Fail(val error: String) : SearchUiState()
}
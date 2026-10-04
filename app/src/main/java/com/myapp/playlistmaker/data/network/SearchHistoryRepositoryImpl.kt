package com.myapp.playlistmaker.data.network

import com.myapp.playlistmaker.data.preferences.SearchHistoryStorage
import com.myapp.playlistmaker.domain.SearchHistoryRepository
import com.myapp.playlistmaker.domain.SearchQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val searchHistoryStorage: SearchHistoryStorage
) : SearchHistoryRepository {

    override suspend fun saveQuery(query: SearchQuery) {
        searchHistoryStorage.saveQuery(query.query)
    }

    override fun observeHistory(): Flow<List<SearchQuery>> = flow {
        val entries = searchHistoryStorage.loadQueries()
        emit(entries.map { SearchQuery(it) })
    }
}
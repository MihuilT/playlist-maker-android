package com.myapp.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun saveQuery(query: SearchQuery)
    fun observeHistory(): Flow<List<SearchQuery>>
}
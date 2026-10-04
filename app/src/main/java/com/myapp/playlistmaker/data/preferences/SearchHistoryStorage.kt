package com.myapp.playlistmaker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchHistoryStorage(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope =
        CoroutineScope(CoroutineName("search-history-storage") + SupervisorJob())
) {

    fun saveQuery(query: String) {
        if (query.isBlank()) return

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[KEY_SEARCH_HISTORY].orEmpty()

                val history = if (historyString.isNotEmpty()) {
                    historyString.split(ENTRY_SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(query)
                history.add(0, query)

                val updated = history
                    .take(MAX_HISTORY_SIZE)
                    .joinToString(ENTRY_SEPARATOR)

                preferences[KEY_SEARCH_HISTORY] = updated
            }
        }
    }

    suspend fun loadQueries(): List<String> {
        val prefs = dataStore.data.first()
        val historyString = prefs[KEY_SEARCH_HISTORY].orEmpty()

        return if (historyString.isEmpty()) {
            emptyList()
        } else {
            historyString.split(ENTRY_SEPARATOR)
        }
    }

    private companion object {
        val KEY_SEARCH_HISTORY = stringPreferencesKey("search_history")
        const val MAX_HISTORY_SIZE = 10
        const val ENTRY_SEPARATOR = ","
    }
}

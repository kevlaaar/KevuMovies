package com.kevlaaar.kevumovies.core.database.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.recentSearchesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "recent_searches"
)

@Singleton
class RecentSearchesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val RECENT_SEARCHES_KEY = stringSetPreferencesKey("recent_searches")
        private const val MAX_RECENT_SEARCHES = 10
    }

    val recentSearches: Flow<List<String>> = context.recentSearchesDataStore.data
        .map { preferences ->
            preferences[RECENT_SEARCHES_KEY]?.toList()?.reversed() ?: emptyList()
        }

    suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        context.recentSearchesDataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()

            currentSearches.remove(query)

            currentSearches.add(query)

            val trimmedSearches = if (currentSearches.size > MAX_RECENT_SEARCHES) {
                currentSearches.toList().takeLast(MAX_RECENT_SEARCHES).toSet()
            } else {
                currentSearches
            }

            preferences[RECENT_SEARCHES_KEY] = trimmedSearches
        }
    }

    suspend fun removeRecentSearch(query: String) {
        context.recentSearchesDataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSearches.remove(query)
            preferences[RECENT_SEARCHES_KEY] = currentSearches
        }
    }

    suspend fun clearRecentSearches() {
        context.recentSearchesDataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES_KEY)
        }
    }























}
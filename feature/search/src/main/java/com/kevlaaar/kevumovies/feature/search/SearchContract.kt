package com.kevlaaar.kevumovies.feature.search

import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.ui.mvi.UiEffect
import com.kevlaaar.kevumovies.core.ui.mvi.UiIntent
import com.kevlaaar.kevumovies.core.ui.mvi.UiState

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Movie> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val isOfflineSearch: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
): UiState {
    val showRecentSearches: Boolean
        get() = query.isEmpty() && recentSearches.isNotEmpty() && !hasSearched

    val showEmptyResults: Boolean
        get() = hasSearched && searchResults.isEmpty() && !isLoading && error == null

    val showResults: Boolean
        get() = searchResults.isNotEmpty()
}

sealed interface SearchIntent: UiIntent {
    data class OnQueryChange(val query: String): SearchIntent
    data class OnSearch(val query: String): SearchIntent
    data class OnRecentSearchClick(val query: String): SearchIntent
    data class OnRemoveRecentSearch(val query: String): SearchIntent
    data object OnClearRecentSearches: SearchIntent
    data class OnMovieClick(val movieId: Int): SearchIntent
    data object OnClearQuery: SearchIntent
    data object OnToggleOfflineSearch: SearchIntent
}

sealed interface SearchEffect: UiEffect {
    data class NavigateToMovieDetail(val movieId: Int): SearchEffect
    data class ShowError(val message: String): SearchEffect
}















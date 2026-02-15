package com.kevlaaar.kevumovies.feature.search

import androidx.lifecycle.viewModelScope
import com.kevlaaar.kevumovies.core.common.network.NetworkMonitor
import com.kevlaaar.kevumovies.core.domain.usecase.GetRecentSearchesUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.ManageRecentSearchesUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.SearchMoviesUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.SearchOfflineMoviesUseCase
import com.kevlaaar.kevumovies.core.ui.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchOfflineMoviesUseCase: SearchOfflineMoviesUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val manageRecentSearchesUseCase: ManageRecentSearchesUseCase,
    private val networkMonitor: NetworkMonitor
) : MviViewModel<SearchUiState, SearchIntent, SearchEffect>(SearchUiState()) {

    private val searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        observeNetworkStatus()
        observerRecentSearches()
        observerSearchQuery()
    }

    override suspend fun handleIntent(intent: SearchIntent) {
        when(intent) {
            is SearchIntent.OnQueryChange -> {
                updateState { copy(query = intent.query, error = null) }
                searchQuery.value = intent.query
            }
            is SearchIntent.OnSearch -> {
                performSearch(intent.query, saveToRecent = true)
            }
            is SearchIntent.OnRecentSearchClick -> {
                updateState { copy(query = intent.query) }
                performSearch(intent.query, saveToRecent = false)
            }
            is SearchIntent.OnRemoveRecentSearch -> {
                manageRecentSearchesUseCase.remove(intent.query)
            }
            SearchIntent.OnClearRecentSearches -> {
                manageRecentSearchesUseCase.clear()
            }
            is SearchIntent.OnMovieClick -> {
                sendEffect(SearchEffect.NavigateToMovieDetail(intent.movieId))
            }
            SearchIntent.OnClearQuery -> {
                updateState {
                    copy(
                        query = "",
                        searchResults = emptyList(),
                        hasSearched = false,
                        error = null
                    )
                }
                searchQuery.value = ""
            }
            SearchIntent.OnToggleOfflineSearch -> {
                val newOfflineState = !currentState.isOfflineSearch
                updateState { copy(isOfflineSearch = newOfflineState) }
                // Retry search with new mode if there is a query
                if(currentState.query.isNotBlank()) {
                    performSearch(currentState.query, saveToRecent = false)
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { isOnline ->
                updateState { copy(isOffline = !isOffline) }
                // Auto-switch to offline search when offline
                if (!isOnline && !currentState.isOfflineSearch) {
                    updateState { copy(isOfflineSearch = true) }
                }
            }
        }
    }

    private fun observerRecentSearches() {
        viewModelScope.launch {
            getRecentSearchesUseCase().collectLatest { searches ->
                updateState { copy(recentSearches = searches) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observerSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    performSearch(query, saveToRecent = false)
                }
        }
    }

    private fun performSearch(query: String, saveToRecent: Boolean) {
        if (query.isBlank()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateState { copy(isLoading = true, error = null, hasSearched = true) }

            val result = if (currentState.isOfflineSearch || currentState.isOffline) {
                searchOfflineMoviesUseCase(query)
            } else {
                searchMoviesUseCase(query)
            }

            result
                .onSuccess { movies ->
                    updateState {
                        copy(
                            searchResults = movies,
                            isLoading = false
                        )
                    }
                    if (saveToRecent && movies.isNotEmpty()) {
                        manageRecentSearchesUseCase.add(query)
                    }
                }
                .onFailure { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = error.message ?: "Search failed"
                        )
                    }
                    sendEffect(SearchEffect.ShowError(error.message ?: "Search failed"))
                }
        }
    }
}
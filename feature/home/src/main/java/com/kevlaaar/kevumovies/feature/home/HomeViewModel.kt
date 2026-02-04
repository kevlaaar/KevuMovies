package com.kevlaaar.kevumovies.feature.home

import androidx.lifecycle.viewModelScope
import com.kevlaaar.kevumovies.core.common.network.NetworkMonitor
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.domain.usecase.GetMoviesByCategoryUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.RefreshMoviesByCategoryUseCase
import com.kevlaaar.kevumovies.core.ui.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val refreshMoviesByCategoryUseCase: RefreshMoviesByCategoryUseCase,
    private val networkMonitor: NetworkMonitor
): MviViewModel<HomeUiState, HomeIntent, HomeEffect>(HomeUiState()) {

    init {
        observerNetworkStatus()
        observeMovies()
        onIntent(HomeIntent.LoadMovies)
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadMovies -> loadAllCategories()
            is HomeIntent.Refresh -> refreshAllCategories()
            is HomeIntent.OnMovieClick -> sendEffect(HomeEffect.NavigateToMovieDetail(intent.movieId))
            is HomeIntent.OnCategoryClick -> sendEffect(HomeEffect.NavigateToCategoryList(intent.category))
        }
    }

    private fun observerNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { isOnline ->
                updateState { copy(isOffline = !isOnline) }
            }
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            getMoviesByCategoryUseCase(MovieListCategory.TRENDING).collectLatest { movies ->
                updateState {
                    copy(trendingMovies = trendingMovies.copy(movies = movies, isLoading = false))
                }
            }

            getMoviesByCategoryUseCase(MovieListCategory.NOW_PLAYING).collectLatest { movies ->
                updateState {
                    copy(nowPlayingMovies = nowPlayingMovies.copy(movies = movies, isLoading = false))
                }
            }

            getMoviesByCategoryUseCase(MovieListCategory.POPULAR).collectLatest { movies ->
                updateState {
                    copy(popularMovies = popularMovies.copy(movies = movies, isLoading = false))
                }
            }

            getMoviesByCategoryUseCase(MovieListCategory.TOP_RATED).collectLatest { movies ->
                updateState {
                    copy(topRatedMovies = topRatedMovies.copy(movies = movies, isLoading = false))
                }
            }

            getMoviesByCategoryUseCase(MovieListCategory.UPCOMING).collectLatest { movies ->
                updateState {
                    copy(upcomingMovies = upcomingMovies.copy(movies = movies, isLoading = false))
                }
            }
        }
    }

    private suspend fun loadAllCategories() {
        val categories = MovieListCategory.entries

        categories.map { category ->
            viewModelScope.async {
                refreshCategory(category, forceRefresh = false)
            }
        }.awaitAll()
    }

    private suspend fun refreshAllCategories() {
        updateState { copy(isRefreshing = true) }

        val categories = MovieListCategory.entries

        categories.map { category ->
            viewModelScope.async {
                refreshCategory(category, forceRefresh = true)
            }
        }.awaitAll()

        updateState { copy(isRefreshing = false) }
    }

    private suspend fun refreshCategory(category: MovieListCategory, forceRefresh: Boolean = false){
        updateCategoryLoading(category, true)

        val result = refreshMoviesByCategoryUseCase(category, forceRefresh = forceRefresh)

        result.onFailure { error ->
            updateCategoryError(category, error.message ?: "Unknown error occurred")
            if(forceRefresh) {
                sendEffect(HomeEffect.ShowError("Failed to refresh ${category.displayName}"))
            }
        }

        updateCategoryLoading(category, false)
    }

    private fun updateCategoryLoading(category: MovieListCategory, isLoading: Boolean) {
        updateState {
            when (category) {
                MovieListCategory.TRENDING -> copy(
                    trendingMovies = trendingMovies.copy(isLoading = isLoading)
                )
                MovieListCategory.NOW_PLAYING -> copy(
                    nowPlayingMovies = nowPlayingMovies.copy(isLoading = isLoading)
                )
                MovieListCategory.POPULAR -> copy(
                    popularMovies = popularMovies.copy(isLoading = isLoading)
                )
                MovieListCategory.TOP_RATED -> copy(
                    topRatedMovies = topRatedMovies.copy(isLoading = isLoading)
                )
                MovieListCategory.UPCOMING -> copy(
                    upcomingMovies = upcomingMovies.copy(isLoading = isLoading)
                )
            }
        }
    }

    private fun updateCategoryError(category: MovieListCategory, error: String) {
        updateState {
            when (category) {
                MovieListCategory.TRENDING -> copy(
                    trendingMovies = trendingMovies.copy(error = error)
                )
                MovieListCategory.NOW_PLAYING -> copy(
                    nowPlayingMovies = nowPlayingMovies.copy(error = error)
                )
                MovieListCategory.POPULAR -> copy(
                    popularMovies = popularMovies.copy(error = error)
                )
                MovieListCategory.TOP_RATED -> copy(
                    topRatedMovies = topRatedMovies.copy(error = error)
                )
                MovieListCategory.UPCOMING -> copy(
                    upcomingMovies = upcomingMovies.copy(error = error)
                )
            }
        }
    }
}
package com.kevlaaar.kevumovies.feature.home

import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.ui.mvi.UiEffect
import com.kevlaaar.kevumovies.core.ui.mvi.UiIntent
import com.kevlaaar.kevumovies.core.ui.mvi.UiState


data class HomeUiState(
    val trendingMovies: MovieListState = MovieListState(),
    val nowPlayingMovies: MovieListState = MovieListState(),
    val popularMovies: MovieListState = MovieListState(),
    val topRatedMovies: MovieListState = MovieListState(),
    val upcomingMovies: MovieListState = MovieListState(),
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false
): UiState {
    val isInitialLoading: Boolean
        get() = trendingMovies.isLoading &&
                nowPlayingMovies.isLoading &&
                popularMovies.isLoading &&
                topRatedMovies.isLoading &&
                upcomingMovies.isLoading &&
                trendingMovies.movies.isEmpty()

    val hasAnyContent: Boolean
        get() = trendingMovies.movies.isNotEmpty() ||
                nowPlayingMovies.movies.isNotEmpty() ||
                popularMovies.movies.isNotEmpty() ||
                topRatedMovies.movies.isNotEmpty() ||
                upcomingMovies.movies.isNotEmpty()
}

data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed interface HomeIntent: UiIntent {
    data object LoadMovies: HomeIntent
    data object Refresh: HomeIntent
    data class OnMovieClick(val movieId: Int): HomeIntent
    data class OnCategoryClick(val category: MovieListCategory): HomeIntent
}

sealed interface HomeEffect: UiEffect {
    data class NavigateToMovieDetail(val movieId: Int): HomeEffect
    data class NavigateToCategoryList(val category: MovieListCategory): HomeEffect
    data class ShowError(val message: String): HomeEffect
}
package com.kevlaaar.kevumovies.feature.details

import com.kevlaaar.kevumovies.core.domain.model.Cast
import com.kevlaaar.kevumovies.core.domain.model.Credits
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.model.Video
import com.kevlaaar.kevumovies.core.ui.mvi.UiEffect
import com.kevlaaar.kevumovies.core.ui.mvi.UiIntent
import com.kevlaaar.kevumovies.core.ui.mvi.UiState

data class DetailsUiState(
    val isLoading: Boolean = true,
    val movieDetail: MovieDetail? = null,
    val credits: Credits? = null,
    val videos: List<Video> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val isFavorite: Boolean = false,
    val error: String? = null
): UiState {
    val trailer: Video?
        get() = videos.firstOrNull() { it.type.name == "TRAILER" && it.isYouTube }
            ?: videos.firstOrNull() { it.isYouTube }

    val topCast: List<Cast>
        get() = credits?.topCast ?: emptyList()

     val director: String?
        get() = credits?.director?.name

    val hasContent: Boolean
        get() = movieDetail != null
}

sealed interface DetailsIntent: UiIntent {
    data class LoadMovieDetails(val movieId: Int): DetailsIntent
    data object ToggleFavorite: DetailsIntent
    data class OnSimilarMoviesClick(val movieId: Int): DetailsIntent
    data class OnVideoClick(val video: Video): DetailsIntent
    data object OnBackClick: DetailsIntent
    data object Retry: DetailsIntent
}

sealed interface DetailsEffect: UiEffect {
    data object NavigateBack: DetailsEffect
    data class NavigateToMovie(val movieId: Int): DetailsEffect
    data class OpenVideoPlayer(val url: String): DetailsEffect
    data class ShowError(val message: String): DetailsEffect
}
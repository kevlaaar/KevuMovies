package com.kevlaaar.kevumovies.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevlaaar.kevumovies.core.domain.usecase.GetMovieCreditsUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.GetMovieDetailUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.GetMovieVideosUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.GetSimilarMoviesUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.IsFavoriteUseCase
import com.kevlaaar.kevumovies.core.domain.usecase.ToggleFavoriteUseCase
import com.kevlaaar.kevumovies.core.ui.mvi.MviViewModel
import com.kevlaaar.kevumovies.feature.details.navigation.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
): MviViewModel<DetailsUiState, DetailsIntent, DetailsEffect>(DetailsUiState()) {

    private val movieId: Int = savedStateHandle.toRoute<MovieDetails>().movieId

    init {
        observeFavoriteStatus()
        onIntent(DetailsIntent.LoadMovieDetails(movieId))
    }

    override suspend fun handleIntent(intent: DetailsIntent) {
        when(intent) {
            is DetailsIntent.LoadMovieDetails -> loadMovieDetails(intent.movieId)
            is DetailsIntent.ToggleFavorite -> toggleFavorite()
            is DetailsIntent.OnSimilarMoviesClick -> {
                sendEffect(DetailsEffect.NavigateToMovie(intent.movieId))
            }
            is DetailsIntent.OnVideoClick -> {
                intent.video.youTubeUrl?.let { url ->
                    sendEffect(DetailsEffect.OpenVideoPlayer(url))
                }
            }
            is DetailsIntent.OnBackClick -> sendEffect(DetailsEffect.NavigateBack)
            is DetailsIntent.Retry -> loadMovieDetails(movieId)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            isFavoriteUseCase(movieId).collectLatest { isFavorite ->
                updateState { copy(isFavorite = isFavorite) }
            }
        }
    }

    private suspend fun loadMovieDetails(movieId: Int) {
        updateState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val detailDeferred = async { getMovieDetailUseCase(movieId) }
            val creditsDeferred = async { getMovieCreditsUseCase(movieId) }
            val videosDeferred = async { getMovieVideosUseCase(movieId) }
            val similarMoviesDeferred = async { getSimilarMoviesUseCase(movieId) }

            val detailResult = detailDeferred.await()
            val creditsResult = creditsDeferred.await()
            val videosResult = videosDeferred.await()
            val similarMoviesResult = similarMoviesDeferred.await()

            detailResult
                .onSuccess { detail ->
                    updateState {
                        copy(
                            isLoading = false,
                            movieDetail = detail,
                            credits = creditsResult.getOrNull(),
                            videos = videosResult.getOrDefault(emptyList()),
                            similarMovies = similarMoviesResult.getOrDefault(emptyList()),
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load movie details"
                        )
                    }
                }
        }
    }

    private suspend fun toggleFavorite() {
        currentState.movieDetail?.let { movieDetail ->
            toggleFavoriteUseCase(movieDetail)
        }
    }
}
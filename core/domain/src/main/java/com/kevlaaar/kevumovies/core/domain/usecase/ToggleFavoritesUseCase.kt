package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoritesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) {
        movieRepository.toggleFavorite(movieId)
    }
}
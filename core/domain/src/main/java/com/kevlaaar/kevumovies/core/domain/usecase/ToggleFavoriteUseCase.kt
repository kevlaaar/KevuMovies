package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieDetail: MovieDetail) {
        movieRepository.toggleFavorite(movieDetail)
    }
}
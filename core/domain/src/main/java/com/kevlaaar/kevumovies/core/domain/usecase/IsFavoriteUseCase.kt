package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Boolean> {
        return movieRepository.isFavorite(movieId)
    }
}
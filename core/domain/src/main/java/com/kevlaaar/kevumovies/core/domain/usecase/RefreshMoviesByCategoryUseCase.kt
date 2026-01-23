package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshMoviesByCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(category: MovieListCategory, page: Int = 1): Result<Unit> {
        return movieRepository.refreshMoviesByCategory(category, page)
    }
}
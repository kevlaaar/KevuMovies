package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(category: MovieListCategory): Flow<List<Movie>> {
        return movieRepository.getMoviesByCategory(category)
    }
}
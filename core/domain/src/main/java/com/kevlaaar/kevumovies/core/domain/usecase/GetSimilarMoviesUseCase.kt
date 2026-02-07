package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<List<Movie>>{
        return movieRepository.getSimilarMovies(movieId)
    }
}
package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class SearchOfflineMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(query: String): Result<List<Movie>>{
        return movieRepository.searchOfflineMovies(query)
    }
}
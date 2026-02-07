package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetail> {
        return movieRepository.getMovieDetail(movieId)
    }
}
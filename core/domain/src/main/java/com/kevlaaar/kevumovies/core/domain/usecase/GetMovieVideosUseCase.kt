package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.model.Video
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<List<Video>> {
        return movieRepository.getMovieVideos(movieId)
    }
}
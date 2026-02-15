package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import javax.inject.Inject

class ManageRecentSearchesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun add(query: String){
        movieRepository.addRecentSearch(query)
    }

    suspend fun remove(query: String) {
        movieRepository.removeRecentSearch(query)
    }

    suspend fun clear() {
        movieRepository.clearRecentSearches()
    }
}
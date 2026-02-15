package com.kevlaaar.kevumovies.core.domain.repository

import com.kevlaaar.kevumovies.core.domain.model.Credits
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesByCategory(category: MovieListCategory): Flow<List<Movie>>

    suspend fun refreshMoviesByCategory(
        category: MovieListCategory,
        page: Int = 1,
        forceRefresh: Boolean = false
    ): Result<Unit>

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>

    suspend fun getMovieCredits(movieId: Int): Result<Credits>

    suspend fun getMovieVideos(movieId: Int): Result<List<Video>>

    suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>>

    suspend fun getRecommendedMovies(movieId: Int): Result<List<Movie>>

    suspend fun searchMovies(query: String, page: Int = 1): Result<List<Movie>>

    suspend fun searchOfflineMovies(query: String): Result<List<Movie>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    suspend fun addToFavorites(movieDetail: MovieDetail)

    suspend fun removeFromFavorites(movieId: Int)

    fun isFavorite(movieId: Int): Flow<Boolean>

    suspend fun toggleFavorite(movieDetail: MovieDetail)

    fun getRecentSearches(): Flow<List<String>>

    suspend fun addRecentSearch(query: String)

    suspend fun removeRecentSearch(query: String)

    suspend fun clearRecentSearches()
}
package com.kevlaaar.core.data.repository

import com.kevlaaar.core.data.mapper.toDomain
import com.kevlaaar.core.data.mapper.toDomainList
import com.kevlaaar.core.data.mapper.toEntitiesWithCategory
import com.kevlaaar.core.data.mapper.toFavoriteEntity
import com.kevlaaar.kevumovies.core.common.di.IoDispatcher
import com.kevlaaar.kevumovies.core.database.dao.FavoriteMovieDao
import com.kevlaaar.kevumovies.core.database.dao.MovieDao
import com.kevlaaar.kevumovies.core.database.datastore.RecentSearchesDataStore
import com.kevlaaar.kevumovies.core.database.entity.MovieCategory
import com.kevlaaar.kevumovies.core.domain.model.Credits
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.domain.model.Video
import com.kevlaaar.kevumovies.core.domain.repository.MovieRepository
import com.kevlaaar.kevumovies.core.network.api.TmdbApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val recentSearchesDataStore: RecentSearchesDataStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): MovieRepository {

    companion object {
        private const val CACHE_DURATION_MS = 30 * 60 * 1000L // 30 minutes
    }

    override fun getMoviesByCategory(category: MovieListCategory): Flow<List<Movie>> {
        return movieDao.observeMoviesByCategory(category.toDatabaseCategory())
            .distinctUntilChanged()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun refreshMoviesByCategory(
        category: MovieListCategory,
        page: Int,
        forceRefresh: Boolean
    ): Result<Unit> = withContext(ioDispatcher){
        runCatching {
            val dbCategory = category.toDatabaseCategory()

            // Check is cache is still valid (and skip check if forceRefresh)
            if(!forceRefresh && page == 1 && isCacheValid(dbCategory)){
                return@runCatching
            }

            val response = when (category) {
                MovieListCategory.NOW_PLAYING -> apiService.getNowPlayingMovies(page)
                MovieListCategory.POPULAR -> apiService.getPopularMovies(page)
                MovieListCategory.TOP_RATED -> apiService.getTopRatedMovies(page)
                MovieListCategory.UPCOMING -> apiService.getUpcomingMovies(page)
                MovieListCategory.TRENDING -> apiService.getTrendingMovies(page = page)
            }

            val (entities, categoryEntities) = response.results.toEntitiesWithCategory(
                category = dbCategory,
                page = page
            )

            if (page == 1) {
                movieDao.refreshMoviesForCategory(dbCategory, entities, categoryEntities)
            } else {
                movieDao.insertMovies(entities)
                movieDao.insertMovieCategories(categoryEntities)
            }
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> =
        withContext(ioDispatcher){
            runCatching {
                val response = apiService.getMovieDetails(movieId)
                val isFavorite = favoriteMovieDao.isFavorite(movieId)
                response.toDomain(isFavorite)
            }
        }

    override suspend fun getMovieCredits(movieId: Int): Result<Credits> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.getMovieCredits(movieId)
                response.toDomain()
            }
        }

    override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> =
        withContext(ioDispatcher){
            runCatching {
                val response = apiService.getMovieVideos(movieId)
                response.results.toDomainList()
            }
        }

    override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> =
        withContext(ioDispatcher){
            runCatching {
                val response = apiService.getSimilarMovies(movieId)
                response.results.toDomainList()
            }
        }

    override suspend fun getRecommendedMovies(movieId: Int): Result<List<Movie>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.getRecommendedMovies(movieId)
                response.results.toDomainList()
            }
        }

    override suspend fun searchMovies(query: String, page: Int): Result<List<Movie>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.searchMovies(query, page)
                response.results.toDomainList()
            }
        }

    override suspend fun searchOfflineMovies(query: String): Result<List<Movie>> =
        withContext(ioDispatcher) {
            runCatching {
                // Search in cached movies
                val cachedResults = movieDao.searchMovies(query).first().map { it.toDomain() }

                // Search in favorites
                val favoriteResults = favoriteMovieDao.searchFavorites(query).map { it.toDomain() }

                val combined = (favoriteResults + cachedResults)
                    .distinctBy { it.id }
                    .sortedByDescending { it.popularity }

                combined
            }
        }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteMovieDao.observeAllFavorites()
            .distinctUntilChanged()
            .map { entities -> entities.toDomainList() }
            .flowOn(ioDispatcher)
    }

    override suspend fun addToFavorites(movieDetail: MovieDetail) =
        withContext(ioDispatcher) {
            val entity = movieDetail.toFavoriteEntity()
            favoriteMovieDao.addFavorite(entity)
        }

    override suspend fun removeFromFavorites(movieId: Int) =
        withContext(ioDispatcher) {
            favoriteMovieDao.removeFavorite(movieId)
        }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
            return favoriteMovieDao.observeIsFavorite(movieId)
                .distinctUntilChanged()
                .flowOn(ioDispatcher)
    }

    override suspend fun toggleFavorite(movieDetail: MovieDetail) =
        withContext(ioDispatcher){
            val isFavorite = favoriteMovieDao.isFavorite(movieDetail.id)
            if(isFavorite){
                favoriteMovieDao.removeFavorite(movieDetail.id)
            } else {
                favoriteMovieDao.addFavorite(movieDetail.toFavoriteEntity())
            }
        }

    override fun getRecentSearches(): Flow<List<String>> {
        return recentSearchesDataStore.recentSearches
    }

    override suspend fun addRecentSearch(query: String) {
        recentSearchesDataStore.addRecentSearch(query)
    }

    override suspend fun removeRecentSearch(query: String) {
        recentSearchesDataStore.removeRecentSearch(query)
    }

    override suspend fun clearRecentSearches() {
        recentSearchesDataStore.clearRecentSearches()
    }

    private suspend fun isCacheValid(category: String): Boolean {
        val lastUpdated = movieDao.getCategoryLastUpdated(category) ?: return false
        return System.currentTimeMillis() - lastUpdated < CACHE_DURATION_MS
    }

    private fun MovieListCategory.toDatabaseCategory(): String {
        return when (this) {
            MovieListCategory.NOW_PLAYING -> MovieCategory.NOW_PLAYING
            MovieListCategory.POPULAR -> MovieCategory.POPULAR
            MovieListCategory.TOP_RATED -> MovieCategory.TOP_RATED
            MovieListCategory.UPCOMING -> MovieCategory.UPCOMING
            MovieListCategory.TRENDING -> MovieCategory.TRENDING
        }
    }
}
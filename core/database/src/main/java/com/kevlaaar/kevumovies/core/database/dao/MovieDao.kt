package com.kevlaaar.kevumovies.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kevlaaar.kevumovies.core.database.entity.MovieCategoryEntity
import com.kevlaaar.kevumovies.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieCategories(categories: List<MovieCategoryEntity>)

    // Query operations
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun observeMovieById(movieId: Int): Flow<MovieEntity?>

    @Transaction
    @Query("""
        SELECT m.* FROM movies m
        INNER JOIN movie_categories mc on m.id = mc.movie_id
        WHERE mc.category = :category
        ORDER BY mc.page ASC, mc.order_index ASC
    """
    )
    fun observeMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Transaction
    @Query("""
        SELECT m.* FROM movies m
        INNER JOIN movie_categories mc ON m.id = mc.movie_id
        WHERE mc.category = :category
        ORDER BY mc.page ASC, mc.order_index ASC
    """)
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    // Search in cached movies
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY popularity DESC")
    fun searchMovies(query: String): Flow<List<MovieEntity>>

    // Cache management
    @Query("DELETE FROM movie_categories WHERE category = :category")
    suspend fun clearMovieCategory(category: String)

    @Query("DELETE FROM movie_categories WHERE category = :category AND movie_id NOT IN (:keepMovieIds)")
    suspend fun clearMovieCategoryExcept(category: String, keepMovieIds: List<Int>)


    @Query("DELETE FROM movies WHERE id NOT IN (SELECT movie_id FROM movie_categories)")
    suspend fun clearOrphanedMovies()

    @Query("SELECT cached_at FROM movie_categories WHERE category = :category LIMIT 1")
    suspend fun getCategoryLastUpdated(category: String): Long?

    @Transaction
    suspend fun refreshMoviesForCategory(
        category: String,
        movies: List<MovieEntity>,
        categoryEntities: List<MovieCategoryEntity>
    ) {
        if(movies.isEmpty()) return
        val newMovieIds = movies.map { it.id }
        insertMovies(movies)
        clearMovieCategoryExcept(category, newMovieIds)
        insertMovieCategories(categoryEntities)
        clearOrphanedMovies()
    }
}
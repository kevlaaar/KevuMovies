package com.kevlaaar.kevumovies.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevlaaar.kevumovies.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeFavorite (movieId: Int)
    @Query("SELECT * FROM favorite_movies ORDER BY favorited_at DESC")
    fun observeAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies ORDER BY favorited_at DESC")
    suspend fun getAllFavorites(): List<FavoriteMovieEntity>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): FavoriteMovieEntity?

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    fun observeFavoriteById(movieId: Int): Flow<FavoriteMovieEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun observeIsFavorite(movieId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Query("SELECT COUNT(*) FROM favorite_movies")
    fun observeFavoriteCount(): Flow<Int>

    @Query("""
            SELECT * FROM favorite_movies
            WHERE title LIKE '%' || :query || '%'
            ORDER BY popularity DESC
        """
    )
    suspend fun searchFavorites(query: String): List<FavoriteMovieEntity>

    // Future: Watchlist support
    @Query("SELECT * FROM favorite_movies WHERE watchlist_id = :watchlistId ORDER BY favorited_at DESC")
    fun observeFavoritesByWatchlist(watchlistId: Long): Flow<List<FavoriteMovieEntity>>

    @Query("UPDATE favorite_movies SET watchlist_id = :watchlistId WHERE id = :movieId")
    suspend fun addToWatchlist(movieId: Int, watchlistId: Long)

    @Query("UPDATE favorite_movies SET watchlist_id = NULL WHERE id = :movieId")
    suspend fun removeFromWatchlist(movieId: Int)
}
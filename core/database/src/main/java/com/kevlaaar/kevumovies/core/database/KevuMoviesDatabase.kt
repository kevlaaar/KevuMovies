package com.kevlaaar.kevumovies.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevlaaar.kevumovies.core.database.dao.FavoriteMovieDao
import com.kevlaaar.kevumovies.core.database.dao.MovieDao
import com.kevlaaar.kevumovies.core.database.dao.RemoteKeyDao
import com.kevlaaar.kevumovies.core.database.entity.FavoriteMovieEntity
import com.kevlaaar.kevumovies.core.database.entity.MovieCategoryEntity
import com.kevlaaar.kevumovies.core.database.entity.MovieEntity
import com.kevlaaar.kevumovies.core.database.entity.RemoteKeyEntity

@Database(
    entities = [
        MovieEntity::class,
        MovieCategoryEntity::class,
        RemoteKeyEntity::class,
        FavoriteMovieEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class KevuMoviesDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}
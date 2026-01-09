package com.kevlaaar.kevumovies.core.database.di

import android.content.Context
import androidx.room.Room
import com.kevlaaar.kevumovies.core.database.KevuMoviesDatabase
import com.kevlaaar.kevumovies.core.database.dao.MovieDao
import com.kevlaaar.kevumovies.core.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): KevuMoviesDatabase {
        return Room.databaseBuilder(
            context,
            KevuMoviesDatabase::class.java,
            "kevu_movies_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: KevuMoviesDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(database: KevuMoviesDatabase): RemoteKeyDao {
        return database.remoteKeyDao()
    }

}
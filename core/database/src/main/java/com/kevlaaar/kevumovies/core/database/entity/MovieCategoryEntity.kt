package com.kevlaaar.kevumovies.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_categories",
    primaryKeys = ["movie_id", "category"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["movie_id"]), Index(value = ["category"])]
)
data class MovieCategoryEntity(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "page")
    val page: Int,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis()
)

object MovieCategory {
    const val NOW_PLAYING = "now_playing"
    const val POPULAR = "popular"
    const val TOP_RATED = "top_rated"
    const val UPCOMING = "upcoming"
    const val TRENDING = "trending"
}
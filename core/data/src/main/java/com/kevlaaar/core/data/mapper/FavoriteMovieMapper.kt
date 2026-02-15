package com.kevlaaar.core.data.mapper

import com.kevlaaar.kevumovies.core.database.entity.FavoriteMovieEntity
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.network.util.ImageUrlBuilder

fun FavoriteMovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = ImageUrlBuilder.buildPosterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.buildBackdropUrl(backdropPath),
        releaseDate = releaseDate,
        releaseYear = releaseDate?.take(4),
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        isFavorite = true
    )
}

fun MovieDetail.toFavoriteEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = extractPathFromUrl(posterUrl),
        backdropPath = extractPathFromUrl(backdropUrl),
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        runtime = runtime,
        genres = genres.joinToString(",") { it.name }
    )
}

private fun extractPathFromUrl(url: String?): String? {
    if (url == null) return null
    // TMDB URL format: https://image.tmdb.org/t/p/w342/posterPath.jpg
    // We need to extract: /posterPath.jpg
    val regex = """/t/p/\w+(/[^/]+)$""".toRegex()
    return regex.find(url)?.groupValues?.get(1)
}

@JvmName("favoriteEntityListToDomain")
fun List<FavoriteMovieEntity>.toDomainList(): List<Movie> = map { it.toDomain() }
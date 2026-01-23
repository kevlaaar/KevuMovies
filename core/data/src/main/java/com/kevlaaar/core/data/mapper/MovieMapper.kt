package com.kevlaaar.core.data.mapper

import com.kevlaaar.kevumovies.core.database.entity.MovieCategoryEntity
import com.kevlaaar.kevumovies.core.database.entity.MovieEntity
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.network.model.MovieDto
import com.kevlaaar.kevumovies.core.network.util.ImageUrlBuilder

fun MovieDto.toDomain(): Movie {
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
        isFavorite = false
    )
}

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}

fun MovieEntity.toDomain(): Movie {
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
        isFavorite = isFavorite
    )
}

fun List<MovieDto>.toEntitiesWithCategory(
    category: String,
    page: Int
): Pair<List<MovieEntity>, List<MovieCategoryEntity>> {
    val entities = map { it.toEntity() }
    val categoryEntities = mapIndexed { index, dto ->
        MovieCategoryEntity(
            movieId = dto.id,
            category = category,
            page = page,
            orderIndex = (page - 1) * 20 + index
        )
    }

    return entities to categoryEntities
}
@JvmName("entityListToDomain")
fun List<MovieEntity>.toDomainList(): List<Movie> = map { it.toDomain() }

@JvmName("dtoListToDomain")
fun List<MovieDto>.toDomainList(): List<Movie> = map { it.toDomain() }
package com.kevlaaar.core.data.mapper

import com.kevlaaar.kevumovies.core.domain.model.Genre
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.model.ProductionCompany
import com.kevlaaar.kevumovies.core.network.model.GenreDto
import com.kevlaaar.kevumovies.core.network.model.MovieDetailDto
import com.kevlaaar.kevumovies.core.network.model.ProductionCompanyDto
import com.kevlaaar.kevumovies.core.network.util.ImageUrlBuilder

fun MovieDetailDto.toDomain(isFavorite: Boolean = false): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterUrl = ImageUrlBuilder.buildPosterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.buildBackdropUrl(
            backdropPath,
            ImageUrlBuilder.ImageSize.BACKDROP_LARGE
        ),
        releaseDate = releaseDate,
        releaseYear = releaseDate?.take(4),
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        budget = budget,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        genres = genres.map { it.toDomain() },
        productionCompanies = productionCompanies.map { it.toDomain() },
        spokenLanguages = spokenLanguages.map { it.englishName },
        homepage = homepage,
        imdbId = imdbId,
        isFavorite = isFavorite
    )
}

fun GenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun ProductionCompanyDto.toDomain(): ProductionCompany {
    return ProductionCompany(
        id = id,
        name = name,
        logoUrl = ImageUrlBuilder.buildPosterUrl(logoPath, ImageUrlBuilder.ImageSize.POSTER_SMALL),
        originCountry = originCountry
    )
}
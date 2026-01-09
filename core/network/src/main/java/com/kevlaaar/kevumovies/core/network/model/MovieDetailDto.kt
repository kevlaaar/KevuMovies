package com.kevlaaar.kevumovies.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("budget")
    val budget: Long,
    @SerialName("revenue")
    val revenue: Long,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String?,
    @SerialName("genres")
    val genres: List<GenreDto>,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageDto>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("imdb_id")
    val imdbId: String?
)

@Serializable
data class GenreDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
data class ProductionCompanyDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class SpokenLanguageDto(
    @SerialName("iso_639_1")
    val iso: String,
    @SerialName("name")
    val name: String,
    @SerialName("english_name")
    val englishName: String
)
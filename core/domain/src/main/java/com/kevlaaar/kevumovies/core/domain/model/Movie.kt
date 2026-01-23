package com.kevlaaar.kevumovies.core.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String?,
    val releaseYear: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val originalLanguage: String,
    val originalTitle: String,
    val isFavorite: Boolean = false
) {
    val ratingPercentage: Int
        get() = (voteAverage * 10).toInt()

    val formattedRating: String
        get() = String.format("%.1f", voteAverage)

    val hasValidPoster: Boolean
        get() = !posterUrl.isNullOrBlank()

    val hasValidBackdrop: Boolean
        get() = !backdropUrl.isNullOrBlank()
}

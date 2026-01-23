package com.kevlaaar.kevumovies.core.domain.model

import android.annotation.SuppressLint

data class MovieDetail(
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
    val budget: Long,
    val revenue: Long,
    val runtime: Int?,
    val status: String,
    val tagline: String?,
    val genres: List<Genre>,
    val productionCompanies: List<ProductionCompany>,
    val spokenLanguages: List<String>,
    val homepage: String?,
    val imdbId: String?,
    val isFavorite: Boolean = false
) {
    val ratingPercentage: Int
        get() = (voteAverage * 10).toInt()

    val formattedRating: String
        get() = String.format("%.1f", voteAverage)

    val formattedRuntime: String?
        get() = runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }

    val formattedBudget: String
        get() = if (budget > 0) formatCurrency(budget) else "N/A"

    val formattedRevenue: String
        get() = if (revenue > 0) formatCurrency(revenue) else "N/A"

    val genreNames: String
        get() = genres.joinToString(", ") { it.name }

    private fun formatCurrency(amount: Long): String {
        return when {
            amount >= 1_000_000_000 -> "$${String.format("%.1f", amount / 1_000_000_000.0)}B"
            amount >= 1_000_000 -> "$${String.format("%.1f", amount / 1_000_000.0)}M"
            amount >= 1_000 -> "$${String.format("%.1f", amount / 1_000.0)}K"
            else -> "$$amount"
        }
    }
}

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoUrl: String?,
    val originCountry: String
)
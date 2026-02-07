package com.kevlaaar.kevumovies.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kevlaaar.kevumovies.feature.details.DetailsRoute
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(val movieId: Int)

fun NavController.navigateToMovieDetails(movieId: Int) {
    navigate(MovieDetails(movieId))
}

fun NavGraphBuilder.detailsScreen(
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    composable<MovieDetails> {
        DetailsRoute(
            onBackClick = onBackClick,
            onMovieClick = onMovieClick
        )
    }
}
package com.kevlaaar.kevumovies.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kevlaaar.kevumovies.feature.favorites.FavoritesRoute
import kotlinx.serialization.Serializable

@Serializable
data object Favorites

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
    navigate(Favorites, navOptions)
}

fun NavGraphBuilder.favoritesScreen(
    onMovieClick: (Int) -> Unit
) {
    composable<Favorites> {
        FavoritesRoute(
            onMovieClick = onMovieClick
        )
    }
}
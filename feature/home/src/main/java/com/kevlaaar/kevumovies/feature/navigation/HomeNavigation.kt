package com.kevlaaar.kevumovies.feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onMovieClick: (Int) -> Unit,
    onCategoryClick: (MovieListCategory) -> Unit
) {
    composable<Home> {
        HomeRoute(
            onMovieClick = onMovieClick,
            onCategoryClick = onCategoryClick
        )
    }
}

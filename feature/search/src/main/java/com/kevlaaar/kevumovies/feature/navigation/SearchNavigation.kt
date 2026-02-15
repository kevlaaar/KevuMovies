package com.kevlaaar.kevumovies.feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kevlaaar.kevumovies.feature.search.SearchRoute
import kotlinx.serialization.Serializable

@Serializable
data object Search

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    navigate(Search, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onMovieClick: (Int) -> Unit
) {
    composable<Search> {
        SearchRoute(
            onMovieClick = onMovieClick
        )
    }
}
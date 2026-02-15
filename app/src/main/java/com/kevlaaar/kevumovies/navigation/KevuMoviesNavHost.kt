package com.kevlaaar.kevumovies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kevlaaar.kevumovies.feature.details.navigation.detailsScreen
import com.kevlaaar.kevumovies.feature.details.navigation.navigateToMovieDetails
import com.kevlaaar.kevumovies.feature.favorites.navigation.favoritesScreen
import com.kevlaaar.kevumovies.feature.navigation.Home
import com.kevlaaar.kevumovies.feature.navigation.homeScreen
import com.kevlaaar.kevumovies.feature.navigation.searchScreen

@Composable
fun KevuMoviesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        homeScreen(
            onMovieClick = { movieId ->
                navController.navigateToMovieDetails(movieId)
            },
            onCategoryClick = {
                // TODO: navigate to catergory list
            }
        )

        searchScreen(
            onMovieClick = { movieId ->
                navController.navigateToMovieDetails(movieId)
            }
        )

        favoritesScreen(
            onMovieClick = { movieId ->
                navController.navigateToMovieDetails(movieId)
            }
        )

        detailsScreen(
            onBackClick = {
                navController.popBackStack()
            },
            onMovieClick = { movieId ->
                navController.navigateToMovieDetails(movieId)
            }
        )
    }
}
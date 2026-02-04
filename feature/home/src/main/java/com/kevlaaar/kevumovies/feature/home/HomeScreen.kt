package com.kevlaaar.kevumovies.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieListCategory
import com.kevlaaar.kevumovies.core.ui.components.LoadingIndicator
import com.kevlaaar.kevumovies.core.ui.components.MovieItem
import com.kevlaaar.kevumovies.core.ui.components.MovieRow
import com.kevlaaar.kevumovies.core.ui.components.OfflineBanner
import com.kevlaaar.kevumovies.core.ui.mvi.CollectEffects
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    onMovieClick: (Int) -> Unit,
    onCategoryClick: (MovieListCategory) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    CollectEffects(viewModel.uiEffect) { effect ->
        when(effect) {
            is HomeEffect.NavigateToMovieDetail -> onMovieClick(effect.movieId)
            is HomeEffect.NavigateToCategoryList -> onCategoryClick(effect.category)
            is HomeEffect.ShowError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KevuMovies",
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isInitialLoading -> {
                    LoadingIndicator()
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { onIntent(HomeIntent.Refresh) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            item {
                                OfflineBanner(isOffline = uiState.isOffline)
                            }

                            item {
                                MovieSection(
                                    title = MovieListCategory.TRENDING.displayName,
                                    state = uiState.trendingMovies,
                                    category = MovieListCategory.TRENDING,
                                    onIntent = onIntent
                                )
                            }

                            item {
                                MovieSection(
                                    title = MovieListCategory.NOW_PLAYING.displayName,
                                    state = uiState.nowPlayingMovies,
                                    category = MovieListCategory.NOW_PLAYING,
                                    onIntent = onIntent
                                )
                            }
                            item {
                                MovieSection(
                                    title = MovieListCategory.POPULAR.displayName,
                                    state = uiState.popularMovies,
                                    category = MovieListCategory.POPULAR,
                                    onIntent = onIntent
                                )
                            }
                            item {
                                MovieSection(
                                    title = MovieListCategory.TOP_RATED.displayName,
                                    state = uiState.topRatedMovies,
                                    category = MovieListCategory.TOP_RATED,
                                    onIntent = onIntent
                                )
                            }
                            item {
                                MovieSection(
                                    title = MovieListCategory.UPCOMING.displayName,
                                    state = uiState.upcomingMovies,
                                    category = MovieListCategory.UPCOMING,
                                    onIntent = onIntent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieSection(
    title: String,
    state: MovieListState,
    category: MovieListCategory,
    onIntent: (HomeIntent) -> Unit
) {
    MovieRow(
        title = title,
        movies = state.movies.toMovieItems(),
        onMovieClick = { movieId -> onIntent(HomeIntent.OnMovieClick(movieId)) },
        onSeeAllClick = { onIntent(HomeIntent.OnCategoryClick(category)) },
        isLoading = state.isLoading
    )
}

private fun List<Movie>.toMovieItems(): List<MovieItem> = map { movie ->
    MovieItem(
        id = movie.id,
        title = movie.title,
        posterUrl = movie.posterUrl,
        rating = movie.voteAverage
    )
}









package com.kevlaaar.kevumovies.feature.details

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kevlaaar.kevumovies.core.domain.model.Cast
import com.kevlaaar.kevumovies.core.domain.model.Movie
import com.kevlaaar.kevumovies.core.domain.model.MovieDetail
import com.kevlaaar.kevumovies.core.domain.model.Video
import com.kevlaaar.kevumovies.core.ui.components.CastCard
import com.kevlaaar.kevumovies.core.ui.components.ErrorContent
import com.kevlaaar.kevumovies.core.ui.components.GenreChip
import com.kevlaaar.kevumovies.core.ui.components.LoadingIndicator
import com.kevlaaar.kevumovies.core.ui.components.MovieCard
import com.kevlaaar.kevumovies.core.ui.components.VideoThumbnail
import com.kevlaaar.kevumovies.core.ui.mvi.CollectEffects

@Composable
fun DetailsRoute(
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            DetailsEffect.NavigateBack -> onBackClick()
            is DetailsEffect.NavigateToMovie -> onMovieClick(effect.movieId)
            is DetailsEffect.OpenVideoPlayer -> {
                val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                context.startActivity(intent)
            }

            is DetailsEffect.ShowError -> {
                // TODO error display
            }
        }
    }

    DetailsScreen(
      uiState = uiState,
      onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    uiState: DetailsUiState,
    onIntent: (DetailsIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onIntent(DetailsIntent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState.hasContent) {
                        IconButton(onClick = {
                            onIntent(DetailsIntent.ToggleFavorite) }) {
                            Icon(
                                imageVector = if (uiState.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Filled.FavoriteBorder
                                },
                                contentDescription = if (uiState.isFavorite) {
                                    "Remove from favorites"
                                } else {
                                    "Add to favorites"
                                },
                                tint = if (uiState.isFavorite) {
                                    Color.Red
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                uiState.error != null -> {
                    ErrorContent(
                        message = uiState.error,
                        onRetry = { onIntent(DetailsIntent.Retry) }
                    )
                }
                uiState.movieDetail != null -> {
                    DetailsContent(
                      movieDetail = uiState.movieDetail,
                      cast = uiState.topCast,
                      director = uiState.director,
                      videos = uiState.videos,
                      similarMovies = uiState.similarMovies,
                      onIntent = onIntent
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailsContent(
    movieDetail: MovieDetail,
    cast: List<Cast>,
    director: String?,
    videos: List<Video>,
    similarMovies: List<Movie>,
    onIntent: (DetailsIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            BackdropHeader(
                backdropUrl = movieDetail.backdropUrl,
                title = movieDetail.title
            )
        }

        item {
            MovieInfoSection(
                movieDetail = movieDetail,
                director = director
            )
        }

        item {
            OverviewSection(overview = movieDetail.overview)
        }

        if(cast.isNotEmpty()) {
            item {
                CastSection(
                    cast = cast
                )
            }
        }

        if(videos.isNotEmpty()) {
            item {
                VideosSection(
                  videos = videos,
                  onVideoClick = { video -> onIntent(DetailsIntent.OnVideoClick(video)) }
                )
            }
        }

        if(similarMovies.isNotEmpty()) {
            item {
                SimilarMoviesSection(
                    movies = similarMovies,
                    onMovieClick = { movieId -> onIntent(DetailsIntent.OnSimilarMoviesClick(movieId)) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun BackdropHeader(
    backdropUrl: String?,
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // Backdrop
        if (backdropUrl != null) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 100f
                    )
                )
        )
    }
}

@Composable
private fun MovieInfoSection(
    movieDetail: MovieDetail,
    director: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = movieDetail.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        movieDetail.tagline?.takeIf { it.isNotBlank() }?.let { tagline ->
            Text(
                text = "\"$tagline\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Year, Runtime, Rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            movieDetail.releaseYear?.let { year ->
                Text(
                    text = year,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            movieDetail.formattedRuntime?.let { runtime ->
                Text(
                    text = runtime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.height(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movieDetail.formattedRating,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "/10",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movieDetail.genres) { genre ->
                GenreChip(genre = genre.name)
            }
        }

        director?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Row{
                Text(
                    text = "Director: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun OverviewSection(overview: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = overview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CastSection(cast: List<Cast>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = cast,
                key = { it.id }
            ) { castMember ->
                CastCard(
                    name = castMember.name,
                    character = castMember.character,
                    profileUrl = castMember.profileUrl
                )
            }
        }
    }
}

@Composable
private fun VideosSection(
    videos: List<Video>,
    onVideoClick: (Video) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Videos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = videos.filter { it.isYouTube },
                key = { it.id }
            ) { video ->
                VideoThumbnail(
                    name = video.name,
                    thumbnailUrl = video.youTubeThumbnailUrl,
                    onClick = { onVideoClick(video) }
                )
            }
        }
    }
}

@Composable
private fun SimilarMoviesSection(
  movies: List<Movie>,
  onMovieClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Similar Movies",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MovieCard(
                    posterUrl = movie.posterUrl,
                    title = movie.title,
                    rating = movie.voteAverage,
                    onClick = { onMovieClick(movie.id) },
                    modifier = Modifier.width(140.dp)
                )
            }
        }
    }
}






















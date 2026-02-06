package com.kevlaaar.kevumovies.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MovieItem(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val rating: Double
)

@Composable
fun MovieRow(
    title: String,
    movies: List<MovieItem>,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    showShimmer: Boolean = false
) {
    val listState = rememberLazyListState()

    var previousFirstMovieId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(movies.firstOrNull()?.id) {
        val currentFirstId = movies.firstOrNull()?.id
        if (currentFirstId != null && previousFirstMovieId != null && currentFirstId != previousFirstMovieId) {
            listState.scrollToItem(0)
        }
        previousFirstMovieId = currentFirstId
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = onSeeAllClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "See all $title",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (showShimmer) {
                MovieRowShimmer()
            } else if(movies.isNotEmpty()) {
                MovieRowContent(
                    movies = movies,
                    listState = listState,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun MovieRowContent(
    movies: List<MovieItem>,
    listState: LazyListState,
    onMovieClick: (Int) -> Unit
) {
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = movies,
            key = { it.id }
        ) { movie ->
            MovieCard(
                posterUrl = movie.posterUrl,
                title = movie.title,
                rating = movie.rating,
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

@Composable
private fun MovieRowShimmer() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(210.dp)
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .matchParentSize()
                )
            }
        }
    }
}
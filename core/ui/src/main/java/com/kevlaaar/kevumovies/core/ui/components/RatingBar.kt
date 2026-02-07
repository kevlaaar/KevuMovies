package com.kevlaaar.kevumovies.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevlaaar.kevumovies.core.ui.theme.RatingGreen

@Composable
fun RatingCircle(
    rating: Double,
    modifier: Modifier = Modifier,
    size: Int = 56
) {
    val percentage = (rating * 10).toInt()
    val color = when {
        percentage >= 70 -> RatingGreen
        percentage >= 50 -> Color(0xFFD2D531)
        else -> Color(0xFFDB2360)
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.size((size - 8).dp),
            color = color,
            strokeWidth = 4.dp,
            trackColor = color.copy(alpha = 0.3f)
        )

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = (size / 4).sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

@Composable
fun GenreChip(
    genre: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = genre,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GenreRow(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.take(3).forEach { genre ->
            GenreChip(genre = genre)
        }
    }
}
package com.kevlaaar.kevumovies.core.domain.model

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: VideoType,
    val official: Boolean,
    val publishedAt: String
) {
    val isYouTube: Boolean
        get() = site.equals("YouTube", ignoreCase = true)

    val youTubeUrl: String?
        get() = if (isYouTube) "https://www.youtube.com/watch?v=$key" else null

    val youTubeThumbnailUrl: String?
        get() = if (isYouTube) "https://img.youtube.com/vi/$key/mqdefault.jpg" else null
}

enum class VideoType {
    TRAILER,
    TEASER,
    CLIP,
    FEATURETTE,
    BEHIND_THE_SCENES,
    BLOOPERS,
    UNKNOWN;

    companion object {
        fun fromString(value: String): VideoType {
            return when (value.lowercase()) {
                "trailer" -> TRAILER
                "teaser" -> TEASER
                "clip" -> CLIP
                "featurette" -> FEATURETTE
                "behind the scenes" -> BEHIND_THE_SCENES
                "bloopers" -> BLOOPERS
                else -> UNKNOWN
            }
        }
    }
}
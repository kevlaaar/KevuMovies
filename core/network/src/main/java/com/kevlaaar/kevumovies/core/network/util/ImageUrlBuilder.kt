package com.kevlaaar.kevumovies.core.network.util

import com.kevlaaar.kevumovies.core.network.BuildConfig

object ImageUrlBuilder {

    private const val BASE_URL = BuildConfig.TMDB_IMAGE_URL

    enum class ImageSize(val path: String) {
        POSTER_SMALL("w185"),
        POSTER_MEDIUM("w342"),
        POSTER_LARGE("w500"),
        POSTER_ORIGINAL("original"),
        BACKDROP_SMALL("w300"),
        BACKDROP_MEDIUM("w780"),
        BACKDROP_LARGE("w1280"),
        BACKDROP_ORIGINAL("original"),
        PROFILE_SMALL("w45"),
        PROFILE_MEDIUM("w185"),
        PROFILE_LARGE("h632"),
        PROFILE_ORIGINAL("original"),
    }

    fun buildPosterUrl(path: String?, size: ImageSize = ImageSize.POSTER_MEDIUM): String?{
        return path?.let { "$BASE_URL${size.path}$it" }
    }

    fun buildBackdropUrl(path: String?, size: ImageSize = ImageSize.BACKDROP_MEDIUM): String?{
        return path?.let { "$BASE_URL${size.path}$it" }
    }

    fun buildProfileUrl(path: String?, size: ImageSize = ImageSize.PROFILE_MEDIUM): String?{
        return path?.let { "$BASE_URL${size.path}$it" }
    }
}
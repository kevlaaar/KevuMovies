package com.kevlaaar.core.data.mapper

import com.kevlaaar.kevumovies.core.domain.model.Video
import com.kevlaaar.kevumovies.core.domain.model.VideoType
import com.kevlaaar.kevumovies.core.network.model.VideoDto

fun VideoDto.toDomain(): Video {
    return Video(
        id = id,
        key = key,
        name = name,
        site = site,
        type = VideoType.fromString(type),
        official = official,
        publishedAt = publishedAt
    )
}

fun List<VideoDto>.toDomainList(): List<Video> = map { it.toDomain() }
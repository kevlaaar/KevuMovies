package com.kevlaaar.core.data.mapper

import com.kevlaaar.kevumovies.core.domain.model.Cast
import com.kevlaaar.kevumovies.core.domain.model.Credits
import com.kevlaaar.kevumovies.core.domain.model.Crew
import com.kevlaaar.kevumovies.core.network.model.CastDto
import com.kevlaaar.kevumovies.core.network.model.CreditsDto
import com.kevlaaar.kevumovies.core.network.model.CrewDto
import com.kevlaaar.kevumovies.core.network.util.ImageUrlBuilder

fun CreditsDto.toDomain(): Credits {
    return Credits(
        cast = cast.map { it.toDomain() },
        crew = crew.map { it.toDomain() }
    )
}

fun CastDto.toDomain(): Cast {
    return Cast(
        id = id,
        name = name,
        character = character,
        profileUrl = ImageUrlBuilder.buildProfileUrl(profilePath),
        order = order
    )
}

fun CrewDto.toDomain(): Crew {
    return Crew(
        id = id,
        name = name,
        job = job,
        department = department,
        profileUrl = ImageUrlBuilder.buildProfileUrl(profilePath)
    )
}
package com.kevlaaar.kevumovies.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsDto(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val cast: List<CastDto>,
    @SerialName("crew")
    val crew: List<CrewDto>
)

@Serializable
data class CastDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("character")
    val character: String,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("order")
    val order: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String
)

@Serializable
data class CrewDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("job")
    val job: String,
    @SerialName("department")
    val department: String,
    @SerialName("profile_path")
    val profilePath: String?
)

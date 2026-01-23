package com.kevlaaar.kevumovies.core.domain.model

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>
) {
    val director: Crew?
        get() = crew.find { it.job.equals("Director", ignoreCase = true) }

    val writers: List<Crew>
        get() = crew.filter {
            it.job.equals("Writer", ignoreCase = true) ||
                    it.job.equals("Screenplay", ignoreCase = true) ||
                    it.department.equals("Writing", ignoreCase = true)
        }.distinctBy { it.id }

    val topCast: List<Cast>
        get() = cast.take(10)
}

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profileUrl: String?,
    val order: Int
) {
    val hasValidProfile: Boolean
        get() = !profileUrl.isNullOrBlank()
}

data class Crew(
    val id: Int,
    val name: String,
    val job: String,
    val department: String,
    val profileUrl: String?
)
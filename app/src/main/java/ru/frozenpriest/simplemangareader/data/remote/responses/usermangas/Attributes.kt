package ru.frozenpriest.simplemangareader.data.remote.responses.usermangas

data class Attributes(
    val altTitles: List<AltTitle>,
    val contentRating: String,
    val createdAt: String,
    val description: Description,
    val isLocked: Boolean,
    val lastChapter: String,
    val lastVolume: String,
    val links: Links,
    val originalLanguage: String,
    val publicationDemographic: String,
    val status: String,
    val tags: List<Tag>,
    val title: Title,
    val updatedAt: String,
    val version: Int,
    val year: Any?
)
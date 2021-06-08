package ru.frozenpriest.simplemangareader.data.models

data class MangaListWithTotal(
    val mangas: List<Manga>,
    val total: Int
)
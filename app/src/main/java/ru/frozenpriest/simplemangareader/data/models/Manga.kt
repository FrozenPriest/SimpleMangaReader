package ru.frozenpriest.simplemangareader.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Manga(
    val name: String?,
    val posterLink: String?,
    val id: String
): Parcelable


val mangas = listOf(
    Manga(name = "Test 1", posterLink = "link1", "null"),
    Manga(name = "Test 2", posterLink = "link2", "null"),
    Manga(name = "Test 3", posterLink = "link3", "null"),
    Manga(name = "Test 4", posterLink = "link4", "null"),
    Manga(name = "Test 11", posterLink = "link11", "null"),
    Manga(name = "Test 12", posterLink = "link12", "null"),
    Manga(name = "Test 13", posterLink = "link13", "null"),
    Manga(name = "Test 14", posterLink = "link14", "null"),
)
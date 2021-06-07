package ru.frozenpriest.simplemangareader.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Manga(
    val name: String?,
    val posterLink: String?
): Parcelable


val mangas = listOf(
    Manga(name = "Test 1", posterLink = "link1"),
    Manga(name = "Test 2", posterLink = "link2"),
    Manga(name = "Test 3", posterLink = "link3"),
    Manga(name = "Test 4", posterLink = "link4"),
    Manga(name = "Test 11", posterLink = "link11"),
    Manga(name = "Test 12", posterLink = "link12"),
    Manga(name = "Test 13", posterLink = "link13"),
    Manga(name = "Test 14", posterLink = "link14"),
)
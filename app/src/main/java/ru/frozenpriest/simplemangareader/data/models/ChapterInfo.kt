package ru.frozenpriest.simplemangareader.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChapterInfo(
    val id: String,
    val title: String,
    val chapter: Double
) : Parcelable

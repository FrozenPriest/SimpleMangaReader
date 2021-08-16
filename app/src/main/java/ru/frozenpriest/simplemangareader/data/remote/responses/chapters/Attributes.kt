package ru.frozenpriest.simplemangareader.data.remote.responses.chapters

data class Attributes(
    val chapter: Double, //from string
    val createdAt: String,
    val `data`: List<String>,
    val dataSaver: List<String>,
    val externalUrl: String,
    val hash: String,
    val publishAt: String,
    val title: String,
    val translatedLanguage: String,
    val updatedAt: String,
    val uploader: String,
    val version: Int,
    val volume: String
)
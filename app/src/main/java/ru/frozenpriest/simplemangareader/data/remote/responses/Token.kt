package ru.frozenpriest.simplemangareader.data.remote.responses

data class Token(
    val session: String,
    val refresh: String,
)
package ru.frozenpriest.simplemangareader.data.remote.responses

data class RefreshTokenResponse(
    val result: String,
    val token: Token,
    val message: String,
)
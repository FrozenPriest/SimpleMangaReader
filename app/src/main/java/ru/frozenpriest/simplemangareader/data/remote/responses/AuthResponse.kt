package ru.frozenpriest.simplemangareader.data.remote.responses

data class AuthResponse(
    val result: String,
    val token: Token
)
package ru.frozenpriest.simplemangareader.data.remote.responses

data class CheckTokenResponse(
    val isAuthenticated: Boolean,
    val permissions: List<String>,
    val result: String,
    val roles: List<String>
)
package ru.frozenpriest.simplemangareader.data.remote.responses.usermangas

data class FollowedManga(
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)
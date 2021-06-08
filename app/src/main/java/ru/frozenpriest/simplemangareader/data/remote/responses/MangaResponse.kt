package ru.frozenpriest.simplemangareader.data.remote.responses

import ru.frozenpriest.simplemangareader.data.remote.responses.usermangas.Result

data class MangaResponse(
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)
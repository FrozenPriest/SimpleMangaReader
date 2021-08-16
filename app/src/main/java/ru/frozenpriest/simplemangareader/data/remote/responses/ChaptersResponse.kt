package ru.frozenpriest.simplemangareader.data.remote.responses

import ru.frozenpriest.simplemangareader.data.remote.responses.chapters.Result

data class ChaptersResponse(
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)
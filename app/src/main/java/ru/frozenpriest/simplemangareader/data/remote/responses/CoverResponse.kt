package ru.frozenpriest.simplemangareader.data.remote.responses

import ru.frozenpriest.simplemangareader.data.remote.responses.cover.CoverData
import ru.frozenpriest.simplemangareader.data.remote.responses.cover.Relationship

data class CoverResponse(
    val `data`: CoverData,
    val relationships: List<Relationship>,
    val result: String
)
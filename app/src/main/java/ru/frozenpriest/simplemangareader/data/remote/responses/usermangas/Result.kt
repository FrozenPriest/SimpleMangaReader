package ru.frozenpriest.simplemangareader.data.remote.responses.usermangas

data class Result(
    val `data`: Data,
    val relationships: List<Relationship>,
    val result: String
)
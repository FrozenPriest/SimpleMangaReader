package ru.frozenpriest.simplemangareader.util

sealed class ResponseResult<T>(val data: T? = null, val message: String = "")

class Success<T>(data: T) : ResponseResult<T>(data)
class Failure<T>(message: String) : ResponseResult<T>(message = message)
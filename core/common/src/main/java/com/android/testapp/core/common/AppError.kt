package com.android.testapp.core.common

sealed interface AppError {
    // Client errors
    data object BadRequest : AppError
    data object Unauthorized : AppError
    data object Forbidden : AppError
    data object NotFound : AppError
    data object Timeout : AppError
    data object PayloadTooLarge : AppError
    data object RateLimited : AppError

    // Server errors
    data object ServerError : AppError
    data object ServiceUnavailable : AppError

    // Transport / parsing
    data object Network : AppError
    data object InvalidData : AppError

    // Unknown
    data class Unknown(val message: String? = null) : AppError
}

fun AppError.asMessage(): String = when(this) {
    AppError.BadRequest -> "Invalid request"
    AppError.Unauthorized -> "You are not authorized"
    AppError.Forbidden -> "Access denied"
    AppError.NotFound -> "GIF not found"
    AppError.Timeout -> "Request timed out"
    AppError.PayloadTooLarge -> "Payload too large"
    AppError.RateLimited -> "Too many requests. Try again later"
    AppError.ServerError -> "Server error"
    AppError.ServiceUnavailable -> "Service unavailable"
    AppError.Network -> "Check your internet connection"
    AppError.InvalidData -> "Received invalid data"
    is AppError.Unknown -> message ?: "Something went wrong"
}
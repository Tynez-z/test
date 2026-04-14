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
package com.android.testapp.core.common

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Failure(val error: AppError) : DataResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): DataResult<R> =
        when (this) {
            is Success -> Success(transform(data))
            is Failure -> this
        }
}
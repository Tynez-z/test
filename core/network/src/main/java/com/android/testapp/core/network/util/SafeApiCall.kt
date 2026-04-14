package com.android.testapp.core.network.util

import com.android.testapp.core.common.AppError
import com.android.testapp.core.common.DataResult
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(apiCall: suspend () -> T, ): DataResult<T> =
    try {
        DataResult.Success(apiCall())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        DataResult.Failure(e.toAppError())
    } catch (e: IOException) {
        DataResult.Failure(AppError.Network)
    } catch (e: SerializationException) {
        DataResult.Failure(AppError.InvalidData)
    } catch (e: Throwable) {
        DataResult.Failure(AppError.Unknown(e.message))
    }

private fun HttpException.toAppError(): AppError =
    when (code()) {
        // 4xx Client errors
        400 -> AppError.BadRequest
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        408 -> AppError.Timeout
        413 -> AppError.PayloadTooLarge
        429 -> AppError.RateLimited

        // 5xx Server errors
        in 500..599 -> when (code()) {
            503 -> AppError.ServiceUnavailable
            504 -> AppError.Timeout
            else -> AppError.ServerError
        }

        // Anything else
        else -> AppError.Unknown("HTTP ${code()}: ${message()}")
    }
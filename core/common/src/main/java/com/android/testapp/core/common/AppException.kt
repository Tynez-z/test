package com.android.testapp.core.common

class AppException(val error: AppError): Exception(error.toString())

// Paging can only carry Throwable
fun Throwable.toAppError(): AppError = (this as? AppException)?.error ?: AppError.Unknown(message)
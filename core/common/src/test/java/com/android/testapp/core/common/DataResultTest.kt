package com.android.testapp.core.common

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataResultTest {
    @Test
    fun dataResultCatchError() = runTest {
        val error = AppError.Unknown("Test Done")
        val result: DataResult<Int> = DataResult.Failure(error)

        val mapped = result.map { it + 1 }
        assertTrue(mapped is DataResult.Failure)
        assertEquals(error, mapped.error)
    }

    @Test
    fun mapTransformsSuccessData() {
        val result: DataResult<Int> = DataResult.Success(1)
        val mapped = result.map { it + 1 }
        assertEquals(DataResult.Success(2), mapped)
    }
}
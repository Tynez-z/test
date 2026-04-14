package com.android.testapp.core.data

import com.android.testapp.core.common.DataResult
import com.android.testapp.core.data.repository.GifRepositoryImpl
import com.android.testapp.core.network.model.gifDetails.GifDetailResponse
import com.android.testapp.core.network.model.searchGif.GifDto
import com.android.testapp.core.network.model.searchGif.GifImagesDto
import com.android.testapp.core.network.model.searchGif.ImageDataDto
import com.android.testapp.core.network.model.searchGif.MetaDto
import com.android.testapp.core.network.service.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class GifRepositoryTests {
    private val api = mockk<ApiService>()
    private val repository = GifRepositoryImpl(api)

    @Test
    fun getGifByIdReturnsSuccessWhenApi200() = runTest {
        val gifDto = GifDto(
            id = "123",
            title = "Funny Cat",
            username = "user",
            importDatetime = "2026-12-01 12:00:00",
            sourcePostUrl = "https://example.com",
            images = GifImagesDto(
                original = ImageDataDto(
                    url = "https://example.com/cat.gif",
                    width = "300",
                    height = "200",
                    size = "1000",
                    webp = "https://example.com/cat.webp",
                    webpSize = "512"
                )
            )
        )
        val metaDto = MetaDto(
            msg = "OK",
            status = 200,
            responseId = "asdasd21312"
        )
        coEvery { api.getGifById("123") } returns GifDetailResponse(data = gifDto, meta = metaDto)

        val result = repository.getGifById("123")

        assertTrue(result is DataResult.Success)
        assertEquals("123", (result as DataResult.Success).data.id)
    }

    @Test
    fun getGifByIdReturnsErrorWhenApiThrows() = runTest {
        coEvery { api.getGifById(any()) } throws IOException("Not found")

        val result = repository.getGifById("invalid_id")

        assertTrue(result is DataResult.Failure)
    }

    @Test
    fun searchGifsReturnsPagingDataFlow() = runTest {
        val flow = repository.searchGifs("cats")
        assertNotNull(flow)

        val job = launch {
            flow.first()
        }
        job.cancel()
    }
}
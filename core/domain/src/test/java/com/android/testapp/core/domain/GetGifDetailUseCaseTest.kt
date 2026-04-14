package com.android.testapp.core.domain

import com.android.testapp.core.common.AppError
import com.android.testapp.core.common.DataResult
import com.android.testapp.core.data.repository.GifRepository
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetGifDetailUseCaseTest {
    private val repository: GifRepository = mockk()
    private val useCase = GetGifDetailUseCase(repository)


    @Test
    fun returnSuccessFromRepository() = runTest {
        val gif = fakeGif()
        coEvery { repository.getGifById("fake-id") } returns DataResult.Success(gif)

        val result = useCase("fake-id")

        assertTrue(result is DataResult.Success)
        assertEquals(gif, (result as DataResult.Success).data)
    }

    @Test
    fun returnErrorFromRepository() = runTest {
        val error = DataResult.Failure(AppError.NotFound)
        coEvery { repository.getGifById(any()) } returns error

        val result = useCase("missing-id")

        assertTrue(result is DataResult.Failure)
    }
}


 fun fakeGif(id: String = "fake-id") = Gif(
        id = "1",
        title = "Cat",
        username = "userCat",
        importDatetime = "2026-12-01 12:00:00",
        sourcePostUrl = "https://example.com",
        images = GifImages(
            original = ImageData(
                url = "https://example.com/cat.gif",
                width = "300",
                height = "200",
                size = "1222222",
                webp = "https://example.com/cat.webp",
                webpSize = "512000"
            ),
            fixedWidth = ImageData(
                url = "https://example.com/cat-small.gif",
                width = "150",
                height = "100",
                size = "256000",
                webp = null,
                webpSize = null
            ),
            fixedHeight = ImageData(
                url = "https://example.com/cat.gif",
                width = "200",
                height = "250",
                size = "123000",
                webp = null,
                webpSize = null
            )
        )
    )

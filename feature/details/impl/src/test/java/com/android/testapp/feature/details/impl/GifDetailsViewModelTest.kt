package com.android.testapp.feature.details.impl

import com.android.testapp.core.common.AppError
import com.android.testapp.core.common.DataResult
import com.android.testapp.core.domain.GetGifDetailUseCase
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class GifDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase: GetGifDetailUseCase = mockk()
    private val gifId = "gif_123"

    private fun createViewModel() = GifDetailsViewModel(useCase, gifId)

    @Test
    fun initialStateLoading() = runTest {
        coEvery { useCase(gifId) } coAnswers { awaitCancellation() }

        val viewModel = createViewModel()
        assertEquals(GifDetailsUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun uiStateSuccessWhenUseCaseReturnSuccess() = runTest {
        val gif = gif
        coEvery { useCase(gifId) } returns DataResult.Success(gif)

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(GifDetailsUiState.Success(gif), viewModel.uiState.value)
    }

    @Test
    fun uiStateErrorWhenUseCaseReturnsFailure() = runTest {
        val error = AppError.ServerError
        coEvery { useCase(gifId) } returns DataResult.Failure(error)

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(GifDetailsUiState.Error(error), viewModel.uiState.value)
    }

    val gif = Gif(
        id = "123ewdsa",
        title = "Cat",
        username = "userCat",
        importDatetime = "2026-13-01 12:00:00",
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
                url = "https://example.com/cat-small.gif",
                width = "150",
                height = "100",
                size = "256000",
                webp = null,
                webpSize = "412000"
            )

        )
    )

}
package com.android.testapp.feature.details.impl

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.testapp.core.common.AppError
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import com.android.testapp.feature.details.impl.components.GifDetailsContent
import com.android.testapp.feature.details.impl.components.GifDetailsErrorContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GifDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeGif = Gif(
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
            ), fixedWidth = ImageData(
                url = "https://example.com/cat-small.gif",
                width = "150",
                height = "100",
                size = "256000",
                webp = null,
                webpSize = null
            ), fixedHeight = ImageData(
                url = "https://example.com/cat.gif",
                width = "200",
                height = "250",
                size = "123000",
                webp = null,
                webpSize = null
            )
        )
    )

    //Content
    @Test
    fun detailsContentShowsTitle() {
        composeTestRule.setContent {
            GifDetailsContent(gif = fakeGif)
        }
        composeTestRule.onNodeWithText("Cat").assertIsDisplayed()
    }

    @Test
    fun detailsContentShowsUsername() {
        composeTestRule.setContent {
            GifDetailsContent(gif = fakeGif)
        }
        composeTestRule.onNodeWithText("userCat").assertIsDisplayed()
    }

    @Test
    fun detailsContent_showsImportDate() {
        composeTestRule.setContent {
            GifDetailsContent(gif = fakeGif)
        }
        composeTestRule.onNodeWithText("2026-12-01 12:00:00").assertIsDisplayed()
    }

    @Test
    fun detailsContentShowsSourceUrl() {
        composeTestRule.setContent {
            GifDetailsContent(gif = fakeGif)
        }
        composeTestRule.onNodeWithText("https://example.com").assertIsDisplayed()
    }

    @Test
    fun detailsContentNoSourceUrl() {
        val gifNoSource = fakeGif.copy(sourcePostUrl = null)
        composeTestRule.setContent {
            GifDetailsContent(gif = gifNoSource)
        }
        composeTestRule.onNodeWithText("Source:", substring = true).assertDoesNotExist()
    }

    //Error

    @Test
    fun detailsErrorShowsErrorMessage() {
        composeTestRule.setContent {
            GifDetailsErrorContent(
                error = AppError.Network,
                onRetry = {},
            )
        }
        // message text from AppError.asMessage()
        composeTestRule.onNodeWithText(AppError.Network.asMessage()).assertIsDisplayed()
    }

    @Test
    fun detailsErrorRetryButtonCallsOnRetry() {
        var retryCalled = false
        composeTestRule.setContent {
            GifDetailsErrorContent(
                error = AppError.Network,
                onRetry = { retryCalled = true },
            )
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryCalled)
    }

    //Back naviagtion
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun detailsScaffoldBackButtonCallsOnBackClick() {
        var backClicked = false
        composeTestRule.setContent {
            val uiState = GifDetailsUiState.Success(fakeGif)
            GifDetailsScreenContent(
                uiState = uiState,
                onBackClick = {
                    backClicked = true
                }, onRetry = {})
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assert(backClicked)
    }
}
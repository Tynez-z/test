package com.android.testapp.feature.search.impl

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import com.android.testapp.feature.search.impl.components.ErrorContent
import com.android.testapp.feature.search.impl.components.PaginationErrorItem
import com.android.testapp.feature.search.impl.components.SearchContent
import com.android.testapp.feature.search.impl.components.SearchTextField
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun fakeGif(id: String) = Gif(
        id = id,
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

    private fun setSearchContent(
        query: String,
        gifs: List<Gif> = emptyList(),
        onGifClick: (String) -> Unit = {},
    ) {
        val pagingData = if (gifs.isEmpty()) {
            PagingData.empty()
        } else {
            PagingData.from(gifs)
        }
        composeTestRule.setContent {
            val fakeFlow = remember { MutableStateFlow(pagingData) }
            val pagingItems = fakeFlow.collectAsLazyPagingItems()
            SearchContent(
                query = query,
                pagingItems = pagingItems,
                onGifClick = onGifClick
            )
        }
    }

    //SearchTextField

    @Test
    fun searchTextFieldDisplaysQuery() {
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "cats",
                onSearchQueryChanged = {},
            )
        }
        composeTestRule
            .onNodeWithTag("searchTextField")
            .assertTextContains("cats")
    }

    @Test
    fun searchTextFieldQueryNotEmpty() {
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "dogs",
                onSearchQueryChanged = {},
            )

        }
        composeTestRule
            .onNodeWithContentDescription("Clear search text")
            .assertIsDisplayed()
    }

    @Test
    fun searchTextFieldQueryEmpty() {
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "",
                onSearchQueryChanged = {},
            )
        }
        composeTestRule
            .onNodeWithContentDescription("query is empty")
            .assertDoesNotExist()
    }

    //Search field content
    @Test
    fun searchContentEmptyPromptQueryIsBlank() {
        setSearchContent(query = "")
        composeTestRule
            .onNodeWithText("What are you looking for?")
            .assertIsDisplayed()
    }

    @Test
    fun searchContentNoResultsQueryNotBlankAndNoItems() {
        setSearchContent(query = "asdasd", gifs = emptyList())
        composeTestRule
            .onNodeWithText("No results for \"asdasd\"", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun searchContentShowsGifGridItemsPresent() {
        val gifs = List(4) {
            fakeGif("$it")
        }
        setSearchContent(query = "cats", gifs = gifs)

        composeTestRule
            .onAllNodes(hasClickAction())
            .assertCountEquals(4)
    }

    // Error, Retry
    @Test
    fun errorContentRetryButtonIsDisplayed() {
        composeTestRule.setContent {
            ErrorContent(onRetry = {})
        }
        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun errorContentRetryButtonCallsOnRetry() {
        var retryCalled = false
        composeTestRule.setContent {
            ErrorContent(onRetry = { retryCalled = true })
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryCalled)
    }

    @Test
    fun paginationErrorItemRetryButtonCallsOnRetry() {
        var retryCalled = false
        composeTestRule.setContent {
            PaginationErrorItem(onRetry = { retryCalled = true })
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryCalled)
    }
}
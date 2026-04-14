package com.android.testapp.feature.search.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import com.android.testapp.feature.search.impl.components.ErrorContent
import com.android.testapp.feature.search.impl.components.SearchContent
import com.android.testapp.feature.search.impl.components.SearchTextField
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    onGifClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val pagingItems = viewModel.gifsPagingData.collectAsLazyPagingItems()

    SearchScreenContent(
        query = query,
        pagingItems = pagingItems,
        onQueryChanged = viewModel::onQueryChanged,
        onGifClick = onGifClick
    )
}

@Composable
internal fun SearchScreenContent(
    query: String,
    pagingItems: LazyPagingItems<Gif>,
    onQueryChanged: (String) -> Unit,
    onGifClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchTextField(
            searchQuery = query,
            onSearchQueryChanged = onQueryChanged,
        )
        SearchContent(
            modifier = Modifier.fillMaxSize(),
            pagingItems = pagingItems,
            query = query,
            onGifClick = onGifClick,
        )
    }
}

/**
 * Previews SearchScreen
 */
@Preview("PreviewSuccess", showBackground = true)
@DevicePreviews
@Composable
private fun SearchScreenContentPreviewSuccess() {
   val gifs = List(6) { index ->
        fakeGif("gif-$index")
    }
    val pagingData = PagingData.from(gifs)
    val fakeFlow = remember { MutableStateFlow(pagingData) }
    val pagingItems = fakeFlow.collectAsLazyPagingItems()

    SearchScreenContent(
        query = "cats",
        pagingItems = pagingItems,
        onQueryChanged = {},
        onGifClick = {},
    )
}

@Preview("PreviewNoResult",showBackground = true)
@DevicePreviews
@Composable
private fun SearchScreenContentPreviewNoResult() {
    val fakeFlow = remember { MutableStateFlow(PagingData.empty<Gif>()) }
    val pagingItems = fakeFlow.collectAsLazyPagingItems()

    SearchScreenContent(
        query = "cats",
        pagingItems = pagingItems,
        onQueryChanged = {},
        onGifClick = {},
    )
}


@Preview("PreviewInitialState",showBackground = true)
@DevicePreviews
@Composable
private fun SearchScreenContentPreviewInitialState() {
    val fakeFlow = remember { MutableStateFlow(PagingData.empty<Gif>()) }
    val pagingItems = fakeFlow.collectAsLazyPagingItems()

    pagingItems.loadState.refresh is LoadState.Error
    SearchScreenContent(
        query = "",
        pagingItems = pagingItems,
        onQueryChanged = {},
        onGifClick = {},
    )
}

@Preview("PreviewError",showBackground = true)
@DevicePreviews
@Composable
private fun SearchContentErrorPreview() {
    Column(Modifier.fillMaxSize()) {
        SearchTextField(
            searchQuery = "adasd",
            onSearchQueryChanged = {},
        )
        ErrorContent(
            onRetry = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

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
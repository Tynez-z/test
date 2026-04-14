package com.android.testapp.feature.details.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.testapp.core.common.AppError
import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import com.android.testapp.feature.details.impl.components.GifDetailsContent
import com.android.testapp.feature.details.impl.components.GifDetailsErrorContent
import com.android.testapp.feature.details.api.R as detailR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GifDetailsScreen(
    viewModel: GifDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GifDetailsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = viewModel::loadGifDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GifDetailsScreenContent(
    uiState: GifDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(detailR.string.feature_details_api_top_app_bar_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(detailR.string.feature_details_api_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is GifDetailsUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is GifDetailsUiState.Success -> {
                    GifDetailsContent(
                        gif = state.gif,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is GifDetailsUiState.Error -> {
                    GifDetailsErrorContent(
                        error = state.error,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


/**
 * Previews GifDetailsScreen and components
 */
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

@Preview("GifDetailsScreenContent")
@DevicePreviews
@Composable
private fun PreviewGifDetailsScreenContent() {
    val uiState = GifDetailsUiState.Success(fakeGif("fake-id"))
    GifDetailsScreenContent(uiState, onBackClick = {}, onRetry = {})
}

@Preview("GifDetailsScreenContent")
@DevicePreviews
@Composable
private fun PreviewGifDetailsScreenContentFailure() {
    val uiStateFailure = GifDetailsUiState.Error(AppError.Network)
    GifDetailsScreenContent(uiStateFailure, onBackClick = {}, onRetry = {})
}

@Preview("GifDetailsScreenContent")
@DevicePreviews
@Composable
private fun PreviewGifDetailsScreenContentLoading() {
    val uiStateFailure = GifDetailsUiState.Loading
    GifDetailsScreenContent(uiStateFailure, onBackClick = {}, onRetry = {})
}

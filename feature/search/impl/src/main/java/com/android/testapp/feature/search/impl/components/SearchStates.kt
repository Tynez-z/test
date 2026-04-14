package com.android.testapp.feature.search.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.testapp.core.model.Gif
import com.android.testapp.feature.search.api.R as searchR

@Composable
internal fun SearchContent(
    query: String,
    pagingItems: LazyPagingItems<Gif>,
    onGifClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when {
            query.isBlank() -> {
                EmptySearchPrompt(modifier = Modifier.fillMaxSize())
            }

            pagingItems.loadState.refresh is LoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            pagingItems.loadState.refresh is LoadState.Error -> {
                ErrorContent(
                    onRetry = pagingItems::retry,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            //NotLoading no active load operation + no error
            pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading -> {
                NoResultsContent(
                    query = query,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                GifGrid(
                    pagingItems = pagingItems,
                    onGifClick = onGifClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
internal fun EmptySearchPrompt(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = stringResource(searchR.string.feature_search_api_what_are_you_looking_for),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
internal fun ErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(searchR.string.feature_search_api_something_went_wrong),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(stringResource(searchR.string.feature_search_api_retry))
            }
        }
    }
}

@Composable
internal fun NoResultsContent(
    query: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(searchR.string.feature_search_api_no_results, query),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}

@Composable
internal fun PaginationErrorItem(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(searchR.string.feature_search_api_failed_load_more),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onRetry) {
            Text(text = stringResource(searchR.string.feature_search_api_retry))
        }
    }
}
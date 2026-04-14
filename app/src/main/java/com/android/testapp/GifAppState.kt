package com.android.testapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.android.testapp.core.data.util.NetworkState
import com.android.testapp.core.navigation.NavigationState
import com.android.testapp.core.navigation.rememberNavigationState
import com.android.testapp.feature.search.api.navigation.SearchNavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GifAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkState: NetworkState
) {
    val isOffline: StateFlow<Boolean> = networkState.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}

@Composable
fun rememberGifAppState(
    networkState: NetworkState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): GifAppState {
    val navigationState = rememberNavigationState(startKey = SearchNavKey)

    return remember(
        navigationState,
        coroutineScope,
        networkState,
    ) {
        GifAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkState = networkState,
        )
    }
}
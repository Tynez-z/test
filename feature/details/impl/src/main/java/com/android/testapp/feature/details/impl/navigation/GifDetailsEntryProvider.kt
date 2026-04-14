package com.android.testapp.feature.details.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.android.testapp.core.navigation.Navigator
import com.android.testapp.feature.details.impl.GifDetailsViewModel
import com.android.testapp.feature.details.api.navigation.GifDetailsNavKey
import com.android.testapp.feature.details.impl.GifDetailsScreen

fun EntryProviderScope<NavKey>.gifDetailsEntry(navigator: Navigator) {
    entry<GifDetailsNavKey> { key ->
        val id = key.gifId
        GifDetailsScreen(
            onBackClick = { navigator.navigateBack() },
            viewModel = hiltViewModel<GifDetailsViewModel,
                    GifDetailsViewModel.Factory>(key = id) { factory ->
                factory.create(id)
            },
        )
    }
}
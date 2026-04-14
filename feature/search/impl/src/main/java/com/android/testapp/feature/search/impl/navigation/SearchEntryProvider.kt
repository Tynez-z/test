package com.android.testapp.feature.search.impl.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.android.testapp.core.navigation.Navigator
import com.android.testapp.feature.details.api.navigation.navigateToDetails
import com.android.testapp.feature.search.api.navigation.SearchNavKey
import com.android.testapp.feature.search.impl.SearchScreen

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchNavKey> {
        SearchScreen(
            onGifClick = navigator::navigateToDetails
        ) }
}
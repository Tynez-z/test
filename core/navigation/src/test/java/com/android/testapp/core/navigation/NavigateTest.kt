package com.android.testapp.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Test

import org.junit.Assert.*

private object SearchScreen : NavKey
private object DetailScreen : NavKey
private data class DetailScreenKey(val gifId: String) : NavKey


class NavigateTest {
    private fun createNavigator(vararg keys: NavKey): Navigator {
        val backStack = NavBackStack(*keys)
        return Navigator(NavigationState(backStack))
    }

    @Test
    fun cannotNavigateBackWhenStartDestination() {
        val navigator = createNavigator(SearchScreen)
        val result = navigator.navigateBack()
        assertFalse(result)
    }

    @Test
    fun navigateToAddsKeyToBackstack() {
        val navigator = createNavigator(SearchScreen)
        navigator.navigateTo(DetailScreen)
        assertEquals(DetailScreen, navigator.state.currentKey)
    }

    @Test
    fun navigateToWithKeyGifId() {
        val navigator = createNavigator(SearchScreen)
        val detailKey = DetailScreenKey(gifId = "abc-123")
        navigator.navigateTo(detailKey)
        assertEquals("abc-123", (navigator.state.currentKey as DetailScreenKey).gifId)
    }

    @Test
    fun navigateBackRemovesLastKeyRestoresPreviousScreen() {
        val navigator = createNavigator(SearchScreen, DetailScreen)
        navigator.navigateBack()
        assertEquals(SearchScreen, navigator.state.currentKey)
    }

}
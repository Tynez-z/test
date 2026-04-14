package com.android.testapp.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation actions by back stack.
 */
class Navigator(val state: NavigationState) {

    fun navigateTo(key: NavKey) {
        state.backStack.add(key)
    }

    fun navigateBack(): Boolean {
        if (state.backStack.size <= 1) return false // false if already at the start destination
        state.backStack.removeLastOrNull()
        return true
    }
}
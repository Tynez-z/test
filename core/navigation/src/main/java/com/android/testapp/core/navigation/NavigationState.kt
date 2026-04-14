package com.android.testapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Holds the app's navigation back stack.
 * For a single-stack app -> one back stack is okay but
 * for a multi-tab app -> multiple NavBackStacks like Map<NavKey, NavBackStack<NavKey>>
 */
class NavigationState(
    val backStack: NavBackStack<NavKey>,
) {
    val currentKey: NavKey get() = backStack.last()
}
/**
 * Creates and remembers a NavigationState that can be save across
 * config changes + process death.
 *
 * @param startKey - initial screen (app launches)
 */
@Composable
fun rememberNavigationState(startKey: NavKey): NavigationState {
    val backStack = rememberNavBackStack(startKey)
    return NavigationState(backStack)
}

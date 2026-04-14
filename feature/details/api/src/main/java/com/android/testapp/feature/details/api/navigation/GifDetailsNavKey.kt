package com.android.testapp.feature.details.api.navigation

import androidx.navigation3.runtime.NavKey
import com.android.testapp.core.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class GifDetailsNavKey(val gifId: String): NavKey {

}
fun Navigator.navigateToDetails(gifId: String) {
    navigateTo(GifDetailsNavKey(gifId))
}
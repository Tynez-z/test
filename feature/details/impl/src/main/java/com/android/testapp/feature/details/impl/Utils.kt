package com.android.testapp.feature.details.impl

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.android.testapp.core.model.Gif

/*
 * Selection strategy:
 * 1. size: "original"
 * 2. format: "webp" over "gif" -> smaller, better compression
 * 3. back to "fixedWidth" size only if a original version is not available
 */
fun selectImageUrl(gif: Gif): String? {
    return gif.images.original?.webp
        ?: gif.images.original?.url
        ?: gif.images.fixedWidth?.webp
        ?: gif.images.fixedWidth?.url
}

@Preview(name = "phone", device = Devices.PHONE, showBackground = true)
@Preview(name = "phone_in_landscape", widthDp = 891, heightDp = 411, showBackground = true)
@Preview(name = "foldable", device = Devices.FOLDABLE, showBackground = true)
@Preview(name = "tablet", device = Devices.TABLET, showBackground = true)
annotation class DevicePreviews
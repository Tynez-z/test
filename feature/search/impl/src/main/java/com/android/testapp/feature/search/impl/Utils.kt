package com.android.testapp.feature.search.impl

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.android.testapp.core.model.Gif

/*
 * Selection strategy:
 * 1. size: "fixedWidth" variants -> resized and lighter than original.
 * 2. format: "webp" over "gif" -> smaller, better compression
 * 3. back to original-size only if a fixedWidth version is not available
 */
internal fun selectImageUrl(gif: Gif): String? {
    return gif.images.fixedWidth?.webp
        ?: gif.images.fixedWidth?.url
        ?: gif.images.original?.webp
        ?: gif.images.original?.url
}

internal fun Gif.aspectRatio(): Float {
    val width = (images.fixedWidth?.width ?: images.original?.width)
        ?.toFloatOrNull() ?: 1f
    val height = (images.fixedWidth?.height ?: images.original?.height)
        ?.toFloatOrNull() ?: 1f
    return (width / height).coerceIn(0.3f, 3f)
}

@Preview(name = "phone", device = Devices.PHONE, showBackground = true)
@Preview(name = "phone_in_landscape", widthDp = 891, heightDp = 411, showBackground = true)
@Preview(name = "foldable", device = Devices.FOLDABLE, showBackground = true)
@Preview(name = "tablet", device = Devices.TABLET, showBackground = true)
annotation class DevicePreviews
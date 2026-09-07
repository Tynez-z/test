package com.android.testapp.core.model

import androidx.compose.runtime.Stable

@Stable
data class Gif(
    val id: String,
    val title: String,
    val username: String,
    val importDatetime: String?,
    val sourcePostUrl: String?,
    val images: GifImages
)

@Stable
data class GifImages(
    val original: ImageData?,
    val fixedHeight: ImageData?,
    val fixedWidth: ImageData?
)

@Stable
data class ImageData(
    val url: String,
    val width: String?,
    val height: String?,
    val size: String?,
    val webp: String?,
    val webpSize: String?
)
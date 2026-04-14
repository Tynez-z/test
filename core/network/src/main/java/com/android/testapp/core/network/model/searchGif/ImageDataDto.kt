package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDataDto(
    val url: String? = null,
    val width: String? = null,
    val height: String? = null,
    val size: String? = null,
    val webp: String? = null,
    @SerialName("webp_size") val webpSize: String? = null
)
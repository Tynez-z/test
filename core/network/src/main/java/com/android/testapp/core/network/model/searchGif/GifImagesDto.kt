package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifImagesDto(
    val original: ImageDataDto,
    @SerialName("fixed_height") val fixedHeight: ImageDataDto? = null,
    @SerialName("fixed_width") val fixedWidth: ImageDataDto? = null,
)
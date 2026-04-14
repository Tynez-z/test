package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifDto(
    val id: String?,
    val title: String? = null,
    val username: String? = null,
    @SerialName("import_datetime") val importDatetime: String? = null,
    @SerialName("source_post_url") val sourcePostUrl: String? = null,
    val images: GifImagesDto
)
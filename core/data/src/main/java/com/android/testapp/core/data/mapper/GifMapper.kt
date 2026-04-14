package com.android.testapp.core.data.mapper

import com.android.testapp.core.model.Gif
import com.android.testapp.core.model.GifImages
import com.android.testapp.core.model.ImageData
import com.android.testapp.core.network.model.searchGif.GifDto
import com.android.testapp.core.network.model.searchGif.GifImagesDto
import com.android.testapp.core.network.model.searchGif.ImageDataDto

fun GifDto.toDomain(): Gif =
    Gif(
        id = requireNotNull(id) { "Gif id must not be null" },
        title = title.orEmpty(),
        username = username.orEmpty(),
        importDatetime = importDatetime,
        sourcePostUrl = sourcePostUrl,
        images = images.toDomain(),
    )

fun GifImagesDto.toDomain(): GifImages =
    GifImages(
        original = original.toDomain(),
        fixedHeight = fixedHeight?.toDomain(),
        fixedWidth = fixedWidth?.toDomain()
    )

fun ImageDataDto.toDomain(): ImageData? {
    val validUrl = url?.takeIf { it.isNotBlank() } ?: return null
    return ImageData(
        url = validUrl,
        width = width,
        height = height,
        size = size,
        webp = webp,
        webpSize = webpSize
    )
}
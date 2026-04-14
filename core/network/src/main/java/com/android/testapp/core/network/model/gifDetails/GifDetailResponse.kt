package com.android.testapp.core.network.model.gifDetails

import com.android.testapp.core.network.model.searchGif.GifDto
import com.android.testapp.core.network.model.searchGif.MetaDto
import kotlinx.serialization.Serializable

@Serializable
data class GifDetailResponse(
    val data: GifDto,
    val meta: MetaDto
)
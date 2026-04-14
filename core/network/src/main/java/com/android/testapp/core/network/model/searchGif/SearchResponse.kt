package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val data: List<GifDto>,
    val pagination: PaginationDto,
    val meta: MetaDto
)
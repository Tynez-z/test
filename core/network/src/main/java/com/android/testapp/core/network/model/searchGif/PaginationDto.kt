package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val offset: Int,
    @SerialName("total_count") val totalCount: Int,
    val count: Int
)
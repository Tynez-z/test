package com.android.testapp.core.network.model.searchGif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    val msg: String,
    val status: Int,
    @SerialName("response_id") val responseId: String
)
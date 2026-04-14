package com.android.testapp.core.data.repository

import androidx.paging.PagingData
import com.android.testapp.core.common.DataResult
import com.android.testapp.core.model.Gif
import kotlinx.coroutines.flow.Flow

interface GifRepository {
    fun searchGifs(query: String): Flow<PagingData<Gif>>
    suspend fun getGifById(gifId: String): DataResult<Gif>
}
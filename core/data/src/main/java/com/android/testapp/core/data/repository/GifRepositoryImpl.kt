package com.android.testapp.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.testapp.core.common.DataResult
import com.android.testapp.core.data.mapper.toDomain
import com.android.testapp.core.data.paging.GifPagingSource
import com.android.testapp.core.model.Gif
import com.android.testapp.core.network.BuildConfig
import com.android.testapp.core.network.service.ApiService
import com.android.testapp.core.network.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GifRepositoryImpl @Inject constructor(
    private val api: ApiService
) : GifRepository {
    override fun searchGifs(query: String): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = GifPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GifPagingSource(
                    api = api,
                    query = query
                )
            })
            .flow
    }

    override suspend fun getGifById(gifId: String): DataResult<Gif> =
        safeApiCall {
            api.getGifById(gifId = gifId)
        }.map { response ->
            response.data.toDomain()
        }
}
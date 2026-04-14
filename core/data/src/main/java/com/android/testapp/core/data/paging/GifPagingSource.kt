package com.android.testapp.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.testapp.core.data.mapper.toDomain
import com.android.testapp.core.model.Gif
import com.android.testapp.core.network.service.ApiService
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class GifPagingSource(
    private val api: ApiService,
    private val query: String
) : PagingSource<Int, Gif>() {
    companion object {
        const val PAGE_SIZE = 20
    }

    /**
     * Api has some bugs - can return duplicate id values across pages.
     *
     * Error looks like: "java.lang.IllegalArgumentException: Key "ctyhZ9zgN3V5DdtVmu" was already
     * used. If you are using LazyColumn/Row please make sure you provide a unique key for each item."
     */
    private val seenIds = mutableSetOf<String>()

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(PAGE_SIZE) ?: anchorPage?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val offset = params.key ?: 0 // how many items to skip from start
        val limit = params.loadSize // how many items to fetch
        return try {
            val response = api.searchGifs(query = query, limit = limit, offset = offset)

            val gifs = response.data
                .map { it.toDomain() }
                .filter { seenIds.add(it.id) }

            val totalCount = response.pagination.totalCount

            val nextOffset = offset + limit

            // 4999 giphy api limitation
            LoadResult.Page(
                data = gifs,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (nextOffset >= totalCount || nextOffset > 4999) {
                    null
                } else {
                    nextOffset
                },
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
package com.android.testapp.core.data

import androidx.paging.PagingSource
import com.android.testapp.core.data.paging.GifPagingSource
import com.android.testapp.core.network.model.searchGif.GifDto
import com.android.testapp.core.network.model.searchGif.GifImagesDto
import com.android.testapp.core.network.model.searchGif.ImageDataDto
import com.android.testapp.core.network.model.searchGif.MetaDto
import com.android.testapp.core.network.model.searchGif.PaginationDto
import com.android.testapp.core.network.model.searchGif.SearchResponse
import com.android.testapp.core.network.service.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class Pagination {
    @Test
    fun loadReturnsPageWhenApi200() = runTest {
        val api = mockk<ApiService>()
        val pagingSource = GifPagingSource(api, "cat")

        coEvery {
            api.searchGifs(query = "cat", limit = 20, offset = 0)
        } returns fakeSearchResponse(totalCount = 100)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(null, result.prevKey)
        assertEquals(20, result.nextKey)
        assertEquals(  result.data.size, result.data.size)
    }

    @Test
    fun loadReturnsErrorOnIOException() = runTest {
        val api = mockk<ApiService>()
        val pagingSource = GifPagingSource(api, "cat")

        coEvery {
            api.searchGifs(any(), any(), any())
        } throws IOException("Network failure")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        result as PagingSource.LoadResult.Error
        assertTrue(result.throwable is IOException)
    }

    @Test
    fun loadReturnsNullNextKeyWhenEndOfListReached() = runTest {
        val api = mockk<ApiService>()
        val pagingSource = GifPagingSource(api, "cat")

        coEvery {
            api.searchGifs(query = "cat", limit = 20, offset = 40)
        } returns fakeSearchResponse(totalCount = 50)

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 40,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(null, result.nextKey)
    }

    @Test
    fun loadReturnsNullNextKeyWhenNextOffsetExceedsGiphyLimit() = runTest {
        val api = mockk<ApiService>()
        val pagingSource = GifPagingSource(api, "cat")

        coEvery {
            api.searchGifs(query = "cat", limit = 20, offset = 4990)
        } returns fakeSearchResponse(totalCount = 9999)

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 4990,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        // 4990 + 20 = 5010 > 4999, so nextKey must be null
        assertNull(result.nextKey)
    }

    private fun fakeSearchResponse(
        totalCount: Int = 100,
        offset: Int = 0,
        count: Int = 20
    ): SearchResponse {
        return SearchResponse(
            data = listOf(
                GifDto(
                    id = "1",
                    title = "Cat",
                    username = "userCat",
                    importDatetime = "2026-12-01 12:00:00",
                    sourcePostUrl = "https://example.com",
                    images = GifImagesDto(
                        original = ImageDataDto(
                            url = "https://example.com/cat.gif",
                            width = "300",
                            height = "200",
                            size = "1222222",
                            webp = "https://example.com/cat.webp",
                            webpSize = "512000"
                        )
                    )
                )
            ),
            pagination = PaginationDto(
                totalCount = totalCount,
                count = count,
                offset = offset
            ),
            meta = MetaDto(
                msg = "OK",
                status = 200,
                responseId = "asdasd21312"
            )
        )
    }
}
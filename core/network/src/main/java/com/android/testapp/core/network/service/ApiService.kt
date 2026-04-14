package com.android.testapp.core.network.service

import com.android.testapp.core.network.model.gifDetails.GifDetailResponse
import com.android.testapp.core.network.model.searchGif.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val SEARCH_GIFS = "gifs/search"
        const val GET_GIF_BY_ID = "gifs/{gif_id}"
        const val GIF_ID = "gif_id"
        const val QUERY = "q"
        const val LIMIT = "limit"
        const val OFFSET = "offset"
    }

    @GET(SEARCH_GIFS)
    suspend fun searchGifs(
        @Query(QUERY) query: String,
        @Query(LIMIT) limit: Int = 20,
        @Query(OFFSET) offset: Int = 0): SearchResponse



    @GET(GET_GIF_BY_ID)
    suspend fun getGifById(
        @Path(GIF_ID) gifId: String): GifDetailResponse
}
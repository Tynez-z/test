package com.android.testapp.core.domain

import androidx.paging.PagingData
import com.android.testapp.core.data.repository.GifRepository
import com.android.testapp.core.model.Gif
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchGifsUseCase @Inject constructor(
    private val repository: GifRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Gif>> {
        return repository.searchGifs(query = query)
    }
}
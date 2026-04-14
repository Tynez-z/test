package com.android.testapp.core.domain

import com.android.testapp.core.common.DataResult
import com.android.testapp.core.data.repository.GifRepository
import com.android.testapp.core.model.Gif
import javax.inject.Inject

class GetGifDetailUseCase @Inject constructor(
    private val repository: GifRepository
) {
    suspend operator fun invoke(gifId: String): DataResult<Gif> {
        return repository.getGifById(gifId)
    }
}
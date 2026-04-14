package com.android.testapp.core.domain

import androidx.paging.PagingData
import com.android.testapp.core.data.repository.GifRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Test

class SearchGifsUseCaseTest {
    private val repository: GifRepository = mockk()
    private val useCase = SearchGifsUseCase(repository)

    @Test
    fun delegatesRepositoryCorrectQuery() = runTest {
        val fakePagingData = PagingData.from(listOf(fakeGif()))
        every { repository.searchGifs("cats") } returns flowOf(fakePagingData)

        useCase("cats")

        verify(exactly = 1) { repository.searchGifs("cats") }
    }

    @Test
    fun returnFlowFromRepository() = runTest {
        val fakePagingData = PagingData.from(listOf(fakeGif()))
        val fakeFlow = flowOf(fakePagingData)
        every { repository.searchGifs("cats") } returns fakeFlow

        val result = useCase("cats")

        assertSame(fakeFlow, result)
    }
}
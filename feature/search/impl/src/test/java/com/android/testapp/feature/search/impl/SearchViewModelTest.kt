package com.android.testapp.feature.search.impl

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.android.testapp.core.domain.SearchGifsUseCase
import com.android.testapp.core.model.Gif
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val searchGifsUseCase: SearchGifsUseCase = mockk()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        every { searchGifsUseCase(any()) } returns flowOf(PagingData.empty())
        viewModel = SearchViewModel(searchGifsUseCase, savedStateHandle)
    }

    @Test
    fun searchQueryInitialValueEmptyString() = runTest {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun onQueryChangedUpdatesSearchQueryInSavedStateHandle() = runTest {
        viewModel.onQueryChanged("cats")
        assertEquals("cats", viewModel.searchQuery.value)
    }

    @Test
    fun gifsPagingDataNotEmitWhenBlankQuery() = runTest {
        val results = mutableListOf<PagingData<Gif>>()
        val job = launch { viewModel.gifsPagingData.toList(results) }

        viewModel.onQueryChanged("   ")
        advanceTimeBy(500)

        assertTrue(results.isEmpty())
        verify { searchGifsUseCase wasNot Called }
        job.cancel()
    }

    @Test
    fun gifsPagingDataEmitsAfterDebounceWhenValidQuery() = runTest {
        val results = mutableListOf<PagingData<Gif>>()
        val job = launch { viewModel.gifsPagingData.toList(results) }

        viewModel.onQueryChanged("cats")
        advanceTimeBy(400)

        assertTrue(results.isNotEmpty())
        verify { searchGifsUseCase("cats") }

        job.cancel()
    }

    @Test
    fun gifsPagingDataNotEmitBeforeDebounceThreshold() = runTest {
        val results = mutableListOf<PagingData<Gif>>()
        val job = launch { viewModel.gifsPagingData.toList(results) }

        viewModel.onQueryChanged("cats")
        advanceTimeBy(200) // below 350ms debounce

        assertTrue(results.isEmpty())
        verify { searchGifsUseCase wasNot Called }

        job.cancel()
    }

    @Test
    fun gifsPagingDataCancelsPreviousSearchWhenQueryChangesFast() = runTest {
        val results = mutableListOf<PagingData<Gif>>()
        val job = launch { viewModel.gifsPagingData.toList(results) }

        viewModel.onQueryChanged("c")
        advanceTimeBy(100)
        viewModel.onQueryChanged("ca")
        advanceTimeBy(100)
        viewModel.onQueryChanged("cats")
        advanceTimeBy(400)

        // Only final query after debounce should call use case
        verify(exactly = 0) { searchGifsUseCase("c") }
        verify(exactly = 0) { searchGifsUseCase("ca") }
        verify(exactly = 1) { searchGifsUseCase("cats") }

        job.cancel()
    }

    @Test
    fun searchQueryRestoredFromSavedStateHandle() = runTest {
        val restoredSavedHandle = SavedStateHandle(mapOf("searchQuery" to "dogs"))
        val restoredViewModel = SearchViewModel(searchGifsUseCase, restoredSavedHandle)

        assertEquals("dogs", restoredViewModel.searchQuery.value)
    }

}
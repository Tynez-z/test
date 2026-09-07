package com.android.testapp.feature.search.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.testapp.core.domain.SearchGifsUseCase
import com.android.testapp.core.model.Gif
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchGifsUseCase: SearchGifsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    val gifsPagingData: Flow<PagingData<Gif>> = searchQuery
        .debounce(350.milliseconds)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { searchQuery ->
            searchGifsUseCase(searchQuery)
        }
        .cachedIn(viewModelScope)

    fun onQueryChanged(newQuery: String) {
        savedStateHandle[SEARCH_QUERY] = newQuery
    }
}

private const val SEARCH_QUERY = "searchQuery"
package com.android.testapp.feature.details.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.testapp.core.common.AppError
import com.android.testapp.core.common.DataResult
import com.android.testapp.core.domain.GetGifDetailUseCase
import com.android.testapp.core.model.Gif
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface GifDetailsUiState {
    data object Loading : GifDetailsUiState
    data class Success(val gif: Gif) : GifDetailsUiState
    data class Error(val error: AppError) : GifDetailsUiState
}

@HiltViewModel(assistedFactory = GifDetailsViewModel.Factory::class)
class GifDetailsViewModel @AssistedInject constructor(
    private val useCase: GetGifDetailUseCase,
    @Assisted val gifId: String) : ViewModel() {

    private val _uiState = MutableStateFlow<GifDetailsUiState>(GifDetailsUiState.Loading)
    val uiState: StateFlow<GifDetailsUiState> = _uiState.asStateFlow()

    init {
        loadGifDetails()
    }

    fun loadGifDetails() {
        viewModelScope.launch {
            _uiState.value = GifDetailsUiState.Loading
            when (val result = useCase(gifId)) {
                is DataResult.Success -> {
                    _uiState.value = GifDetailsUiState.Success(result.data)
                }
                is DataResult.Failure -> {
                    _uiState.value = GifDetailsUiState.Error(result.error)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(gifId: String): GifDetailsViewModel
    }

}
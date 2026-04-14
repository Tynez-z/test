package com.android.testapp.core.data.util

import kotlinx.coroutines.flow.Flow

interface NetworkState {
    val isOnline: Flow<Boolean>
}
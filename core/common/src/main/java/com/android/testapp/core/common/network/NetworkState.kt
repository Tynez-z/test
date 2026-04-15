package com.android.testapp.core.common.network

import kotlinx.coroutines.flow.Flow

interface NetworkState {
    val isOnline: Flow<Boolean>
}
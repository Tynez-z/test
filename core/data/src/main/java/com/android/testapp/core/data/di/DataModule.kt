package com.android.testapp.core.data.di

import com.android.testapp.core.common.network.NetworkState
import com.android.testapp.core.data.repository.GifRepository
import com.android.testapp.core.data.repository.GifRepositoryImpl
import com.android.testapp.core.data.util.ConnectivityManagerNetworkState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindGifRepository(impl: GifRepositoryImpl): GifRepository

    @Binds
    @Singleton
    internal abstract fun bindNetworkState(networkState: ConnectivityManagerNetworkState): NetworkState
}
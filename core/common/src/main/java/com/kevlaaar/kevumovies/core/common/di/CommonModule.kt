package com.kevlaaar.kevumovies.core.common.di

import com.kevlaaar.kevumovies.core.common.network.ConnectivityManagerNetworkMonitor
import com.kevlaaar.kevumovies.core.common.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

}
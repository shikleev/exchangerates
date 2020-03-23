package com.shikleev.exchangerates.di

import com.shikleev.exchangerates.data.network.NetworkService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkService() = NetworkService.retrofitService()

}
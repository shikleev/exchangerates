package com.shikleev.exchangerates.di

import android.content.Context
import com.google.gson.Gson
import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.data.datastore.DataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: App) {

    @Provides
    @Singleton
    fun provideApp(): App = application

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideDataBase(): DataStore = application.dataStore

}
package com.shikleev.exchangerates

import android.app.Application
import androidx.room.Room
import com.shikleev.exchangerates.data.datastore.DataStore
import com.shikleev.exchangerates.di.ApplicationComponent
import com.shikleev.exchangerates.di.ApplicationModule
import com.shikleev.exchangerates.di.DaggerApplicationComponent

class App : Application() {

    lateinit var dataStore: DataStore

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDataStore()
        initDagger()
    }

    private fun initDagger() {
        component = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        component.inject(this)
    }

    private fun initDataStore() {
        dataStore = Room.databaseBuilder(this, DataStore::class.java, "datastore").build()
    }
}
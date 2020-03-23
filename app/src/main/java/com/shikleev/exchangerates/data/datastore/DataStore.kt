package com.shikleev.exchangerates.data.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shikleev.exchangerates.data.datastore.typeconverter.RatesConverter
import com.shikleev.exchangerates.data.model.Rate

@TypeConverters(RatesConverter::class)
@Database(entities = [Rate::class], version = 1)
abstract class DataStore : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}
package com.shikleev.exchangerates.data.datastore

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shikleev.exchangerates.data.model.Rate

@Dao
interface RatesDao {
    @Query("SELECT * FROM rate ORDER BY date DESC")
    fun getRates(): LiveData<Rate>

    @Query("DELETE FROM rate")
    fun deleteAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: Rate)
}
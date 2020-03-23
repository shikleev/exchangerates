package com.shikleev.exchangerates.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Rate(
    @PrimaryKey
    val id: Long,
    @SerializedName("base")
    val base: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("rates")
    val rates: Rates?
) {
    data class Rates(
        @SerializedName("EUR")
        val eur: Double?,
        @SerializedName("USD")
        val usd: Double?
    )
}
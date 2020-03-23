package com.shikleev.exchangerates.data.datastore.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shikleev.exchangerates.data.model.Rate

class RatesConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun fromEntity(entity: Rate.Rates?): String? {
            if (entity == null) {
                return null
            }
            return Gson().toJson(entity)
        }

        @TypeConverter
        @JvmStatic
        fun toEntity(entity: String?): Rate.Rates? {
            if (entity == null) {
                return null
            }
            val entityType = object : TypeToken<Rate.Rates>() {}.type
            return Gson().fromJson(entity, entityType)
        }
    }

}
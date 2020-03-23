package com.shikleev.exchangerates.data.network

import com.shikleev.exchangerates.data.model.Rate
import com.shikleev.exchangerates.data.model.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface MainApi {

    @GET("latest")
    suspend fun getLatest(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Rate?

//    @POST("likes.add")
//    suspend fun addLike(
//            @Query(VKApiConst.VERSION) versionApi: String,
//            @Query("type") type: String,
//            @Query("owner_id") ownerId: Long,
//            @Query("item_id") itemId: Long
//    ): ResponseWrapper<Any?>

}
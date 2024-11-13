package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.MarketPagingData
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.liseening.LiseeningData
import com.pedpo.pedporent.model.liseening.RequestLiseening
import com.pedpo.pedporent.view.paging.liseening.model.RequestPagingLiseen
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface LiSeeningAPI {

    @POST("Liseening/CreateLiseening")
    suspend fun createLiSeening(@Body liseening: RequestLiseening):Response<ResponseTO>

    @POST("Liseening/GetLiseeningLists_Items")
    suspend fun itemsLiSeening(@Body request: RequestPagingLiseen):Response<MarketPagingData>?

    @FormUrlEncoded
    @POST("Liseening/DeleteLiseening")
    suspend fun deleteLiSeening(@Field("ID") id:String):Response<ResponseTO>

    @FormUrlEncoded
    @POST("Liseening/GetLiseeningLists")
    suspend fun getLiseens(@Field("Type") type:String ):Response<LiseeningData>



}
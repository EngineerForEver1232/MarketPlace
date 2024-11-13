package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.model.MapResponse
import com.pedpo.pedporent.model.model.OneMarketMap
import com.pedpo.pedporent.model.model.RequestMapTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface MapApi {

    @POST("Map/ListMarkets")
    suspend fun markets(@Body requestMapTO: RequestMapTO?):Response<MapResponse>

    @POST("Map/ListServices")
    suspend fun services(@Body requestMapTO: RequestMapTO?):Response<MapResponse>

    @FormUrlEncoded
    @POST("Map/OneMarket")
    suspend fun oneMarkets(@Field("CityID") cityID:String?):Response<OneMarketMap>

}
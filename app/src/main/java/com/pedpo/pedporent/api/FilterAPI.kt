package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.MarketPagingData
import com.pedpo.pedporent.model.filter.RequestFilter
import com.pedpo.pedporent.model.filter.TitleSearchFilter
import com.pedpo.pedporent.model.filter.car.RequestFilterAdvancedCar
import com.pedpo.pedporent.model.filter.home.RequestFilterAdvancedHome
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface FilterAPI {

    @POST("Search/SearchByTitle")
    suspend fun searchByTitle(@Body requestFilter: TitleSearchFilter?): Response<MarketPagingData?>?;

    @POST("Search/SearchByFilter")
    suspend fun filterSearch(@Body requestFilter: RequestFilter?): Response<MarketPagingData?>?;


    @POST("Search/SearchByFilterService")
    suspend fun filterSearchService(@Body requestFilter: RequestFilter?): Response<MarketPagingData?>?;


    @POST("Search/AdvanceSearchHomeByFilter")
    suspend fun filterAdvancedHome(@Body requestFilterAdvancedHome: RequestFilterAdvancedHome): Response<MarketPagingData?>?;


    @POST("Search/AdvanceSearchCarByFilter")
    suspend fun filterAdvancedCar(@Body requestCar: RequestFilterAdvancedCar): Response<MarketPagingData?>?;

}
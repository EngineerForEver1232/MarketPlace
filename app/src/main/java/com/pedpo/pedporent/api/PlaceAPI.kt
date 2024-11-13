package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.changeData.ChangeDataCategory
import com.pedpo.pedporent.model.changeData.ChangeDataPlace
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.local.PlaceDataLocal
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface PlaceAPI {

    @GET("Location/GetAllCountry")
    suspend fun getCountreis(): Response<PlaceData>

    @FormUrlEncoded
    @POST("Location/GetProvincebyCountryID")
    suspend fun getProvinces(@Field("CountryID") countryID:String,
                             @Field("Title") title:String?
    ): Response<PlaceData>

    @FormUrlEncoded
    @POST("Location/GetCitybyProvinceID?ProvinceID")
    suspend fun getCities(@Field("ProvinceID") proviceID:String,
                          @Field("Title") title:String?): Response<PlaceData>

    @GET("Location/GetAllLocations")
    suspend fun getPlaces():PlaceDataLocal;

    @GET("Changes/GetChangesLocation")
    suspend fun changeData():Response<ChangeDataPlace>


}
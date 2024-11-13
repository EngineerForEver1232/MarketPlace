package com.pedpo.pedporent.model.place.local

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.room.entity.place.CityEntity
import com.pedpo.pedporent.room.entity.place.CountryEntity
import com.pedpo.pedporent.room.entity.place.ProvinceEntity

data class PlaceAllData(

    @SerializedName("city_List")
    var cityList: List<CityEntity>,
    @SerializedName("province_List")
    var provinceList: List<ProvinceEntity>,
    @SerializedName("country_List")
    var countryList: List<CountryEntity>

)
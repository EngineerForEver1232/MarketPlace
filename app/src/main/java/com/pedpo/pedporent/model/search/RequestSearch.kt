package com.pedpo.pedporent.model.search

import com.google.gson.annotations.SerializedName

data class RequestSearch(
    @SerializedName("Title")
    var title:String,
    @SerializedName("CityID")
    var cityID:String

)
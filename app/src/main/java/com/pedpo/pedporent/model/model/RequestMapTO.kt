package com.pedpo.pedporent.model.model

import com.google.gson.annotations.SerializedName

data class RequestMapTO (

    @SerializedName("CityID")
    private var cityID:String,
    @SerializedName("CategoryID")
    private var categoryID:String?=null,
    @SerializedName("IP")
    private var iP:String,
    @SerializedName("Type")
     var type:String,
    @SerializedName("LatSouthwest")
    private var latSouthwest:Double,
    @SerializedName("LngSouthwest")
    private var lngSouthwest:Double,
    @SerializedName("LatNortheast")
    private var latNortheast:Double,
    @SerializedName("LngNortheast")
    private var lngNortheast:Double

)
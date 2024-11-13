package com.pedpo.pedporent.model.liseening

import com.google.gson.annotations.SerializedName

data class RequestLiseening (

    @SerializedName("Type")
    var type: String,
    @SerializedName("CityID")
    var cityID: String?=null,
    @SerializedName("CategoryID")
    var categoryID: String?=null,
    @SerializedName("PriceFrom")
    var priceFrom: Long,
    @SerializedName("PriceTo")
    var priceTo: Long,
    @SerializedName("PriceAgree")
    var priceAgree: Boolean,
    @SerializedName("Free")
    var free: Boolean,
    @SerializedName("TypeOFPrice")
    var typeOFPrice: String // for renteral / hourly , monthly , yearly

)
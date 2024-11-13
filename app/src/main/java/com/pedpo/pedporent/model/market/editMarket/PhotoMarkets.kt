package com.pedpo.pedporent.model.market.editMarket

import com.google.gson.annotations.SerializedName

data class PhotoMarkets(
    @SerializedName("Type")
    var type:String?,
    @SerializedName("Photo")
    var photo:String?,
    @SerializedName("Order")
    var order:Int?
)
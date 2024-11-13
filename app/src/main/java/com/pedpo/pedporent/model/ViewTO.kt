package com.pedpo.pedporent.model

import com.google.gson.annotations.SerializedName

data class ViewTO(
    @SerializedName("MarketID")
    var marketID:String,
    @SerializedName("IP")
    var iP:String,
    @SerializedName("Type")
    var type:String,

    var neighborMarketID:Int?=null
)
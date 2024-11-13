package com.pedpo.pedporent.model.store

import com.google.gson.annotations.SerializedName

data class RateStoreTO (
    @SerializedName("StoreID")
    var storeID:String?="",
    @SerializedName("IP")
    var ip:String?="",
    @SerializedName("Rate")
    var rate:Int?=0
        )
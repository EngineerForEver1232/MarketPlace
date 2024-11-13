package com.pedpo.pedporent.model

import com.google.gson.annotations.SerializedName

data class ActiveMarketTO (

    var MarketID:String,
    var StartTimeInactive:String,
    var EndTimeInactive:String,
    @SerializedName("Type")
    var typeAPI:String


)
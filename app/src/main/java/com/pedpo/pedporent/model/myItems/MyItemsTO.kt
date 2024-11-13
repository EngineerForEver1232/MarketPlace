package com.pedpo.pedporent.model.myItems

import com.google.gson.annotations.SerializedName

data class MyItemsTO(

    @SerializedName("listRenterMarketsViewModel")
    var myMarkets:List<MyMarkets>,

    @SerializedName("listServicesViewModel")
    var myService:List<MyMarkets>

)
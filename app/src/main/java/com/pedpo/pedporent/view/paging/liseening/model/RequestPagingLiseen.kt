package com.pedpo.pedporent.view.paging.liseening.model

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO

data class RequestPagingLiseen (

    @SerializedName("Paging")
    var paging: RequestPaginTO?=null,
    @SerializedName("ID")
    var id:String?=null,
    @SerializedName("IP")
    var ip:String?=null

)
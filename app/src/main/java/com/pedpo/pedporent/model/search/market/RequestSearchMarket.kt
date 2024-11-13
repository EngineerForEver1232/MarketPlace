package com.pedpo.pedporent.model.search.market

import com.google.gson.annotations.SerializedName

data class RequestSearchMarket(
    var title: String?,
    @SerializedName("CategoryID")
    var categoryID: String?,
    @SerializedName("CityID")
    var cityID: String?,
    @SerializedName("PagingParameters")
    var paging: PagingSearch?,
    @SerializedName("IP")
    var ip: String?=null

)
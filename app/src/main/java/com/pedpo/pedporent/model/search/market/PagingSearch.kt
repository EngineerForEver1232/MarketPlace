package com.pedpo.pedporent.model.search.market

import com.google.gson.annotations.SerializedName

data class PagingSearch(
    @SerializedName("PageNumber")
    var pageNumber: String,
    @SerializedName("PageSize")
    var pageSize: String
)
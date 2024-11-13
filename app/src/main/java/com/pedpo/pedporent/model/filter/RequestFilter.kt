package com.pedpo.pedporent.model.filter

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO

data class RequestFilter(

    @SerializedName("InputFilter")
    var inputFilter:InputFilter,
    @SerializedName("Paging")
    var Paging: PagingNumber


)
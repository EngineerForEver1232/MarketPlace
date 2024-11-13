package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model

import com.google.gson.annotations.SerializedName

class RequestPaginTO {

    @SerializedName("PageNumber")
    var pageNumber:String?=null;
    @SerializedName("PageSize")
    var pageSize:String?=null;


    constructor(pageNumber: String?, pageSize: String?) {
        this.pageNumber = pageNumber
        this.pageSize = pageSize
    }
}
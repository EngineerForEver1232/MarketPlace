package com.pedpo.pedporent.view.paging.store.model

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO

class StorePagingData {

    @SerializedName("Paging")
    var paging: RequestPaginTO?=null;
    @SerializedName("StoreID")
    var storeID:String?=null;
//    @SerializedName("CityID")
//    var cityID:String?=null;
    @SerializedName("CategoryID")
    var categoryID:String?=null;
    @SerializedName("IP")
    var ip:String?=null;
    @SerializedName("Type")
    var typeAPI:String?=null;
    @SerializedName("Title")
    var title:String?=null;


    constructor(
        paging: RequestPaginTO,
        title: String?,
        categoryID: String?,
        storeID: String?,
        ip: String?,
        typeAPI: String?
    ){
        this.paging = paging
        this.title = title
        this.categoryID = categoryID;
        this.storeID = storeID
        this.ip = ip
        this.typeAPI = typeAPI
    }
}
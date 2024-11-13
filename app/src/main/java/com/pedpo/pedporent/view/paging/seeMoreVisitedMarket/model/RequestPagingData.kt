package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model

import com.google.gson.annotations.SerializedName

class RequestPagingData {

    @SerializedName("Paging")
    var paging:RequestPaginTO?=null;
    @SerializedName("CityID")
    var cityID:String?=null;
    @SerializedName("CategoryID")
    var categoryID:String?=null;
    @SerializedName("SubCategoryID")
    var subCategoryID:String?=null;
    @SerializedName("IP")
    var ip:String?=null;
    @SerializedName("Type")
    var typeAPI:String?=null;


    constructor(paging: RequestPaginTO?, cityID: String?, categoryID: String?,ip:String?) {
        this.paging = paging
        this.cityID = cityID
        this.categoryID = categoryID
        this.ip = ip
    }
    constructor(paging: RequestPaginTO?, cityID: String?, categoryID: String?,subCategoryID:String?,ip:String?) {
        this.paging = paging
        this.cityID = cityID
        this.subCategoryID = subCategoryID
        this.ip = ip
    }
//    constructor(paging: RequestPaginTO?, cityID: String?, categoryID: String?,subCategoryID:String?, ip:String?, typeAPI:String?) {
//        this.paging = paging
//        this.cityID = cityID
//        this.subCategoryID = subCategoryID
//        this.ip = ip
//        this.typeAPI = typeAPI
//    }

    constructor(
        paging: RequestPaginTO,
        cityID: String?,
        categoryID: String?,
        subCategoryID: Nothing?,
        ip: String?,
        typeAPI: String?
    ){
        this.paging = paging
        this.cityID = cityID
        this.categoryID = categoryID;
        this.subCategoryID = subCategoryID
        this.ip = ip
        this.typeAPI = typeAPI
    }
}
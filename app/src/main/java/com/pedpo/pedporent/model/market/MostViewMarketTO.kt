package com.pedpo.pedporent.model.market

import com.google.gson.annotations.SerializedName

class MostViewMarketTO {

    @SerializedName("CityID")
    var cityID:String?=null;
    @SerializedName("CategoryID")
    var categoryID:String?=null;

    var marketID:String?=null;
    var title:String?=null;
    var photoAddress:String?=null;
    var scoreView:Int?=null;
    var registerDate:String?=null;
    var categoryName:String?=null;
    var isActive:Boolean?=null;



}
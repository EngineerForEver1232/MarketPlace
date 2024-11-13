package com.pedpo.pedporent.model.filter

import com.google.gson.annotations.SerializedName

data class InputFilter(

    @SerializedName("title")
    var title:String,
    @SerializedName("CityID")
    var cityID:String?=null,
    @SerializedName("CategoryID")
    var categoryID:String?=null,
    @SerializedName("SubCategoryID")
    var subCategoryID: String?=null,
    @SerializedName("Type")
    var type:String,
    @SerializedName("PriceFrom")
    var priceFrom:Long?=null,
    @SerializedName("PriceTo")
    var priceTo:Long?=null,
    @SerializedName("PriceAgree")
    var priceAgree:Boolean,
    @SerializedName("Free")
    var free:Boolean,
    @SerializedName("RegisterDate")
    var registerDate:String?=null,
    @SerializedName("TypeOFPrice")
    var typeOfPrice:String,
    @SerializedName("IP")
    var iP:String

)
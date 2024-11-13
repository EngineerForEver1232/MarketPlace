package com.pedpo.pedporent.model.store

import android.net.Uri
import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.market.PhotoMarketsTO

 class StoreRequest {

    @SerializedName("Title")
    var title:String?=null;
    @SerializedName("Description")
    var description:String?=null;
    @SerializedName("Phone")
    var phone:String?=null;
    @SerializedName("Inner")
    var sendingInner: Boolean?=false;
    @SerializedName("Onner")
    var sendingOuner: Boolean?=false;
    @SerializedName("Email")
    var email: String?=null;
    @SerializedName("Latitude")
    var latitude: String?=null;
    @SerializedName("Longitude")
    var longitude: String?=null;
    @SerializedName("CityID")
    var cityID: String?=null;
    @SerializedName("Logo")
    var logo: String?=null;
    @SerializedName("Madrak")
    var madrak: String?=null;
    @SerializedName("CategoryStoreID")
    var categoryID: String?=null;

    @SerializedName("StorePhotoes")
    var storePhotoes:List<PhotoStoreTO>?=null
    @SerializedName("CategoryList")
    var categoryList:List<String>?=null
    @SerializedName("Roles")
    var roles:String?=""


}
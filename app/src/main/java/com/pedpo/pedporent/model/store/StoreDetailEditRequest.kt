package com.pedpo.pedporent.model.store

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.store.edit.Logo
import com.pedpo.pedporent.model.store.edit.MadrakStore
import com.pedpo.pedporent.model.store.edit.PhotoStore

class StoreDetailEditRequest  {

    @SerializedName("Title")
    var title:String?=null;
    @SerializedName("Roles")
    var role:String?=null;
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
    @SerializedName("CategoryList")
    var categoryList:List<String>?=null;

//    var listCategoryID:List<String>?=null
//    var listCategoryName:List<String>?=null

    @SerializedName("logo")
    var logo:Logo?=null;


}
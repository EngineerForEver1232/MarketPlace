package com.pedpo.pedporent.model.store

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.store.edit.Logo
import com.pedpo.pedporent.model.store.edit.MadrakStore
import com.pedpo.pedporent.model.store.edit.PhotoStore

class StoreRequestEdit  {

    @SerializedName("StoreID")
    var storeID:String?=null;
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
    @SerializedName("CategoryStoreID")
    var categoryID: String?=null;
    @SerializedName("Madrak")
    var madrak: MadrakStore?=null;
    @SerializedName("Photo")
    var photo: List<PhotoStore>?=null;
    @SerializedName("DeleteURL")
    var deleteURL:List<String>?=null;

    @SerializedName("CategoryList")
    var categoryList:List<String>?=null

//    var listCategoryID:List<String>?=null
//    var listCategoryName:List<String>?=null

    @SerializedName("logo")
    var logo:Logo?=null;


}
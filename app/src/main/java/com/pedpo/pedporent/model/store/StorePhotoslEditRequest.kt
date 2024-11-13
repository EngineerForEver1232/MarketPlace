package com.pedpo.pedporent.model.store

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.store.edit.Logo
import com.pedpo.pedporent.model.store.edit.MadrakStore
import com.pedpo.pedporent.model.store.edit.PhotoStore

class StorePhotoslEditRequest  {



    @SerializedName("logo")
    var logo:Logo?=null;
    @SerializedName("Madrak")
    var madrak: MadrakStore?=null;
    @SerializedName("Photo")
    var photo: List<PhotoStore>?=null;
    @SerializedName("DeleteURL")
    var deleteURL:List<String>?=null;



}
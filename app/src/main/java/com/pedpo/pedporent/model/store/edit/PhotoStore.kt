package com.pedpo.pedporent.model.store.edit

import com.google.gson.annotations.SerializedName

data class PhotoStore(
    @SerializedName("Type")
    var type:String?,
    @SerializedName("Photo")
    var photo:String?,
    @SerializedName("Order")
    var order:Int?
)
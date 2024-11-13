package com.pedpo.pedporent.model.store

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PhotoStoreTO(

    @SerializedName("Order")
    var order:Int?=null,
    @SerializedName("Image")
    var photo:String?=null


)
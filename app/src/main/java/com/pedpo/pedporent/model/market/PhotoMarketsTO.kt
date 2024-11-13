package com.pedpo.pedporent.model.market

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PhotoMarketsTO() : Parcelable{

    @SerializedName("order")
    var order:Int?=null;
    @SerializedName("Photo")
    var photo:String?=null;
    @SerializedName("Type")
    var type:String?=null;

    var uri:Uri?=null;

    constructor(parcel: Parcel) : this() {
        order = parcel.readValue(Int::class.java.classLoader) as? Int
        photo = parcel.readString()
        type = parcel.readString()
        uri = parcel.readParcelable(Uri::class.java.classLoader)
    }


    constructor(order:Int,photo:String?) : this() {
        this.order = order;
        this.photo = photo;
    }
    constructor(order:Int,bitmap: Uri) : this() {
        this.order = order;
        this.uri = bitmap;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(order)
        parcel.writeString(photo)
        parcel.writeString(type)
        parcel.writeParcelable(uri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoMarketsTO> {
        override fun createFromParcel(parcel: Parcel): PhotoMarketsTO {
            return PhotoMarketsTO(parcel)
        }

        override fun newArray(size: Int): Array<PhotoMarketsTO?> {
            return arrayOfNulls(size)
        }
    }


}
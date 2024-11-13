package com.pedpo.pedporent.model.market.editMarket

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable


class PhotoEditTO() :Parcelable{
     var image: String? = null
     var order: Int?=null

    constructor(parcel: Parcel) : this() {
        image = parcel.readString()
        order = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeValue(order)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoEditTO> {
        override fun createFromParcel(parcel: Parcel): PhotoEditTO {
            return PhotoEditTO(parcel)
        }

        override fun newArray(size: Int): Array<PhotoEditTO?> {
            return arrayOfNulls(size)
        }
    }
}

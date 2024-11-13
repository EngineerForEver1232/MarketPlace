package com.pedpo.pedporent.model.details

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PhotoDetailTO() :Parcelable{

//    @SerializedName("Image")
    var image:String?=null;
    var order:Int?=null;

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

    companion object CREATOR : Parcelable.Creator<PhotoDetailTO> {
        override fun createFromParcel(parcel: Parcel): PhotoDetailTO {
            return PhotoDetailTO(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDetailTO?> {
            return arrayOfNulls(size)
        }
    }

}
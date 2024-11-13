package com.pedpo.pedporent.model.store.edit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Logo() : Parcelable {

    @SerializedName("Photo")
    var photo:String?=""
    @SerializedName("Type")
    var type:String?=""

    constructor(parcel: Parcel) : this() {
        photo = parcel.readString()
        type = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photo)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Logo> {
        override fun createFromParcel(parcel: Parcel): Logo {
            return Logo(parcel)
        }

        override fun newArray(size: Int): Array<Logo?> {
            return arrayOfNulls(size)
        }
    }

}
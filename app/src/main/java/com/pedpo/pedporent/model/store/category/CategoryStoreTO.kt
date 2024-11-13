package com.pedpo.pedporent.model.store.category

import android.os.Parcel
import android.os.Parcelable

class CategoryStoreTO() : Parcelable {


    var categoryStoreID:String?=null;
    var appIconeAddress:String?=null;
    var categoryStoreName:String?=null;

    var selected:Boolean?=false;

    constructor(parcel: Parcel) : this() {
        categoryStoreID = parcel.readString()
        appIconeAddress = parcel.readString()
        categoryStoreName = parcel.readString()
        selected = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryStoreID)
        parcel.writeString(appIconeAddress)
        parcel.writeString(categoryStoreName)
        parcel.writeValue(selected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryStoreTO> {
        override fun createFromParcel(parcel: Parcel): CategoryStoreTO {
            return CategoryStoreTO(parcel)
        }

        override fun newArray(size: Int): Array<CategoryStoreTO?> {
            return arrayOfNulls(size)
        }
    }


}
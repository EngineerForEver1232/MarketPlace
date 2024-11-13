package com.pedpo.pedporent.view.store

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.pedpo.pedporent.model.store.category.CategoryStoreTO

class TransferStore() :Parcelable {
    var title:String?=null;
    var description:String?=null;
    var phone:String?=null;
    var email:String?=null;
    var categoryID:String?=null;
    var uri: Uri?=null;
    var listCategorySelected:List<String>?=null;

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        phone = parcel.readString()
        email = parcel.readString()
        categoryID = parcel.readString()
        uri = parcel.readParcelable(Uri::class.java.classLoader)
        listCategorySelected = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(categoryID)
        parcel.writeParcelable(uri, flags)
        parcel.writeStringList(listCategorySelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransferStore> {
        override fun createFromParcel(parcel: Parcel): TransferStore {
            return TransferStore(parcel)
        }

        override fun newArray(size: Int): Array<TransferStore?> {
            return arrayOfNulls(size)
        }
    }


}
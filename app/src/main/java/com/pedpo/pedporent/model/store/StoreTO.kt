package com.pedpo.pedporent.model.store

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class StoreTO() :Parcelable{
     var storeID: String?=null;
     var title: String?=null;
     var roles: String?=null;
     var description: String?=null;
     var phone: String?=null;
     var inner: Boolean?=false;
     var onner: Boolean?=false;
     var madrak: String?=null;
     var logo: String?=null;
     var logoUri: Uri?=null;
     var email: String?=null;
     var latitude: String?=null;
     var longitude: String?=null;
     var cityName: String?=null;
     var cityID: String?=null;
     var rateStore: Int?=0;


    var listCategoryID:List<String>?=null
    var listCategoryName:List<String>?=null

    constructor(parcel: Parcel) : this() {
        storeID = parcel.readString()
        title = parcel.readString()
        description = parcel.readString()
        phone = parcel.readString()
        inner = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        onner = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        madrak = parcel.readString()
        logo = parcel.readString()
        logoUri = parcel.readParcelable(Uri::class.java.classLoader)
        email = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
        cityName = parcel.readString()
        cityID = parcel.readString()
        rateStore = parcel.readValue(Int::class.java.classLoader) as? Int
        listCategoryID = parcel.createStringArrayList()
        listCategoryName = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storeID)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(phone)
        parcel.writeValue(inner)
        parcel.writeValue(onner)
        parcel.writeString(madrak)
        parcel.writeString(logo)
        parcel.writeParcelable(logoUri, flags)
        parcel.writeString(email)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(cityName)
        parcel.writeString(cityID)
        parcel.writeValue(rateStore)
        parcel.writeStringList(listCategoryID)
        parcel.writeStringList(listCategoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoreTO> {
        override fun createFromParcel(parcel: Parcel): StoreTO {
            return StoreTO(parcel)
        }

        override fun newArray(size: Int): Array<StoreTO?> {
            return arrayOfNulls(size)
        }
    }


}
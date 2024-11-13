package com.pedpo.pedporent.model.market

import android.os.Parcel
import android.os.Parcelable

class TypeOfguaranteeTO():Parcelable {

//    @SerializedName("Check")
    var check:Boolean?=false;
//    @SerializedName("PromissoryNote")
    var promissoryNote:Boolean?=false;
//    @SerializedName("NationalCard")
    var nationalCard:Boolean?=false;
//    @SerializedName("IdentityCard")
    var identityCard:Boolean?=false;
//    @SerializedName("Other")
    var other:Boolean?=false;
//    @SerializedName("OtherText")
    var otherText:String?=null;

    constructor(parcel: Parcel) : this() {
        check = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        promissoryNote = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        nationalCard = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        identityCard = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        other = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        otherText = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(check)
        parcel.writeValue(promissoryNote)
        parcel.writeValue(nationalCard)
        parcel.writeValue(identityCard)
        parcel.writeValue(other)
        parcel.writeString(otherText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypeOfguaranteeTO> {
        override fun createFromParcel(parcel: Parcel): TypeOfguaranteeTO {
            return TypeOfguaranteeTO(parcel)
        }

        override fun newArray(size: Int): Array<TypeOfguaranteeTO?> {
            return arrayOfNulls(size)
        }
    }


}
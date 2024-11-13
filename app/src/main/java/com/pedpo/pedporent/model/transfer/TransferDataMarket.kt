package com.pedpo.pedporent.model.transfer

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.model.market.editMarket.PhotoEditTO

class TransferDataMarket() : Parcelable {

    var title:String?=null;// force
    var description:String?=null;// force
    var agreement:String?=null; // not force
    var type:String?=null;// force
    var showType:String?=null;// force
    var rentPrice:String?=null;// force
//    var commodityPrice:String?=null;// gheymat kala // force
    var priceAgree:Boolean?=false;// force
    var free:Boolean?=false;// force

    var cityName: String? = null ;
    var cityID: String? = null ;
    var latitude: String? = null ;
    var longitude: String? = null  ;


    var isHome:Boolean?=null;// force
    var meterOfHouse: Int? = 0;
    var yearOfHouse: String? = null;
    var rooms: Int? = 0;
    var bathrooms: Int? = 0;
    var parkingTypeName: String? = null;
    var parkingID: String? = null;
    var heatingName: String? = null;
    var heatingID: String? = null;
    var coolingName: String? = null;
    var coolingID: String? = null;


    var isCar:Boolean?=null;// force
    var fuelType: String? = null;
    var kilometerOfCar: Int? = 0;
    var yearOfCar: String? = null;


    var categoryID: String? = null;
    var subCategroyID: String? = null;
    var categoryName: String? = null;



    var salePrice: Long? = null;
    var priceDayli: String? = null;
    var commodityPrice: String? = null;

    var hourlyRentalPrice: Long? = 0;
    var monthlyRentalPrice: Long? = 0;
    var yearlyRentPrice: Long? = 0;


    var assignment: String? = "0";

    var listPhotos:ArrayList<PhotoTO>?=null;// force
    var photos:ArrayList<PhotoEditTO>?=null;// force


    @SerializedName("typeOfguarantee")
    var typeOfguaranteeTO: TypeOfguaranteeTO?=null;// force

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        agreement = parcel.readString()
        type = parcel.readString()
        showType = parcel.readString()
        rentPrice = parcel.readString()
        priceAgree = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        free = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        cityName = parcel.readString()
        cityID = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
        isHome = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        meterOfHouse = parcel.readValue(Int::class.java.classLoader) as? Int
        yearOfHouse = parcel.readString()
        rooms = parcel.readValue(Int::class.java.classLoader) as? Int
        bathrooms = parcel.readValue(Int::class.java.classLoader) as? Int
        parkingTypeName = parcel.readString()
        parkingID = parcel.readString()
        heatingName = parcel.readString()
        heatingID = parcel.readString()
        coolingName = parcel.readString()
        coolingID = parcel.readString()
        isCar = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        fuelType = parcel.readString()
        kilometerOfCar = parcel.readValue(Int::class.java.classLoader) as? Int
        yearOfCar = parcel.readString()
        categoryID = parcel.readString()
        subCategroyID = parcel.readString()
        categoryName = parcel.readString()
        salePrice = parcel.readValue(Long::class.java.classLoader) as? Long
        priceDayli = parcel.readString()
        commodityPrice = parcel.readString()
        hourlyRentalPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        monthlyRentalPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        yearlyRentPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        assignment = parcel.readString()
        typeOfguaranteeTO = parcel.readParcelable(TypeOfguaranteeTO::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(agreement)
        parcel.writeString(type)
        parcel.writeString(showType)
        parcel.writeString(rentPrice)
        parcel.writeValue(priceAgree)
        parcel.writeValue(free)
        parcel.writeString(cityName)
        parcel.writeString(cityID)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeValue(isHome)
        parcel.writeValue(meterOfHouse)
        parcel.writeString(yearOfHouse)
        parcel.writeValue(rooms)
        parcel.writeValue(bathrooms)
        parcel.writeString(parkingTypeName)
        parcel.writeString(parkingID)
        parcel.writeString(heatingName)
        parcel.writeString(heatingID)
        parcel.writeString(coolingName)
        parcel.writeString(coolingID)
        parcel.writeValue(isCar)
        parcel.writeString(fuelType)
        parcel.writeValue(kilometerOfCar)
        parcel.writeString(yearOfCar)
        parcel.writeString(categoryID)
        parcel.writeString(subCategroyID)
        parcel.writeString(categoryName)
        parcel.writeValue(salePrice)
        parcel.writeString(priceDayli)
        parcel.writeString(commodityPrice)
        parcel.writeValue(hourlyRentalPrice)
        parcel.writeValue(monthlyRentalPrice)
        parcel.writeValue(yearlyRentPrice)
        parcel.writeString(assignment)
        parcel.writeParcelable(typeOfguaranteeTO, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransferDataMarket> {
        override fun createFromParcel(parcel: Parcel): TransferDataMarket {
            return TransferDataMarket(parcel)
        }

        override fun newArray(size: Int): Array<TransferDataMarket?> {
            return arrayOfNulls(size)
        }
    }


}
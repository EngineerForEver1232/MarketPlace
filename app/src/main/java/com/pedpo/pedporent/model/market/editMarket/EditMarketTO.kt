package com.pedpo.pedporent.model.market.editMarket

import android.os.Parcel
import android.os.Parcelable
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.google.gson.annotations.SerializedName

class EditMarketTO() : Parcelable {

    var title:String?=null;// force
    var description:String?=null;// force
    var agreement:String?=null; // not force
    var latitude:String?=null;// force
    var longitude:String?=null;// force
    var cityID:String?=null;// force
    var categoryID:String?=null;// force
    var type:String?=null;// force
    var subCategroyID:String?=null;// force
    var rentPrice:String?=null;// force
    var hourlyRentalPrice:Long?=null;// force
    var monthlyRentalPrice:Long?=null;// force
    var yearlyRentPrice:Long?=null;// force


    var salePrice:Long?=null;// force

    var priceAgree:Boolean?=null;// force
    var free:Boolean?=null;// force

    var isHome:Boolean?=null;// force
    var meterOfHouse:Long?=null;// force
    var rooms:Int?=null;// force
    var yearOfHouse:Int?=null;// force
    var bathrooms:Int?=null;// force  var yearOfHouse:Int?=null;// force
    var parkingTypeName:String?=null;// gheymat kala // force
    var parkingID:Int?=null;// gheymat kala // force
    var heatingName:String?=null;// gheymat kala // force
    var heatingID:Int?=null;// gheymat kala // force
    var coolingName:String?=null;// gheymat kala // force
    var coolingID:Int?=null;// gheymat kala // force

    var isCar:Boolean?=null;// force
    var kilometerOfCar:Int?=null;// gheymat kala // force
    var fuelType:String?=null;// gheymat kala // force
    var yearOfCar:String?=null;// gheymat kala // force

    var categoryName:String?=null;// gheymat kala // force
    var subCategory:String?=null;// gheymat kala // force

    var cityName:String?=null

    var commodityPrice:String?=null;// gheymat kala // force
    @SerializedName("typeOfguarantee")
    var typeOfguaranteeTO: TypeOfguaranteeTO?=null;// force
    var photos:List<PhotoEditTO>?=null;// force

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        agreement = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
        cityID = parcel.readString()
        categoryID = parcel.readString()
        subCategroyID = parcel.readString()
        rentPrice = parcel.readString()
        hourlyRentalPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        monthlyRentalPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        yearlyRentPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        salePrice = parcel.readValue(Long::class.java.classLoader) as? Long
        priceAgree = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        free = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isHome = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        meterOfHouse = parcel.readValue(Long::class.java.classLoader) as? Long
        rooms = parcel.readValue(Int::class.java.classLoader) as? Int
        bathrooms = parcel.readValue(Int::class.java.classLoader) as? Int
        parkingTypeName = parcel.readString()
        heatingName = parcel.readString()
        coolingName = parcel.readString()
        isCar = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        kilometerOfCar = parcel.readValue(Int::class.java.classLoader) as? Int
        fuelType = parcel.readString()
        yearOfCar = parcel.readString()
        categoryName = parcel.readString()
        subCategory = parcel.readString()
        cityName = parcel.readString()
        commodityPrice = parcel.readString()
        typeOfguaranteeTO = parcel.readParcelable(TypeOfguaranteeTO::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(agreement)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(cityID)
        parcel.writeString(categoryID)
        parcel.writeString(subCategroyID)
        parcel.writeString(rentPrice)
        parcel.writeValue(hourlyRentalPrice)
        parcel.writeValue(monthlyRentalPrice)
        parcel.writeValue(yearlyRentPrice)
        parcel.writeValue(salePrice)
        parcel.writeValue(priceAgree)
        parcel.writeValue(free)
        parcel.writeValue(isHome)
        parcel.writeValue(meterOfHouse)
        parcel.writeValue(rooms)
        parcel.writeValue(bathrooms)
        parcel.writeString(parkingTypeName)
        parcel.writeString(heatingName)
        parcel.writeString(coolingName)
        parcel.writeValue(isCar)
        parcel.writeValue(kilometerOfCar)
        parcel.writeString(fuelType)
        parcel.writeString(yearOfCar)
        parcel.writeString(categoryName)
        parcel.writeString(subCategory)
        parcel.writeString(cityName)
        parcel.writeString(commodityPrice)
        parcel.writeParcelable(typeOfguaranteeTO, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EditMarketTO> {
        override fun createFromParcel(parcel: Parcel): EditMarketTO {
            return EditMarketTO(parcel)
        }

        override fun newArray(size: Int): Array<EditMarketTO?> {
            return arrayOfNulls(size)
        }
    }


}
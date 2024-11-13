package com.pedpo.pedporent.model.market

import android.os.Parcel
import android.os.Parcelable
import com.pedpo.pedporent.model.ResponseTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class AdMarketTO() : ResponseTO(),Parcelable {

     @SerializedName("Title")
     var title:String?=null;// force
     @SerializedName("Description")
     var description:String?=null;// force
     @SerializedName("Agreement")
     var agreement:String?=null; // not force
     @SerializedName("Latitude")
     var latitude:String?=null;// force
     @SerializedName("Longitude")
     var longitude:String?=null;// force
     @SerializedName("CityID")
     var cityID:String?=null;// force
     @SerializedName("CategoryMarketID")
     var categoryMarketID:String?=null;// force
     @SerializedName("PriceAgree")
     var priceAgree:Boolean?=null;// force
     @SerializedName("Free")
     var free:Boolean?=null;// force

    @SerializedName("CommodityPrice")
    var commodityPrice:Long?=null;// gheymat kala // force
    @SerializedName("SubCategoryMarketID")
    var subCategoryMarketID:String?=null;// force
    @SerializedName("typeOfguarantee")
    var typeOfguaranteeTO:TypeOfguaranteeTO?=null;// force
    @SerializedName("PhotoMarkets")
    var listPhotos:List<PhotoMarketsTO>?=null;// force

    @SerializedName("ShowType")
    var showType:String?=null;
    @SerializedName("Type")
    var type:String?=null;
    @SerializedName("SalePrice")
    var salePrice:Long?=null;
    @SerializedName("RentPrice")
    var rentPrice:Int?=0;// force , daily price , gheimat rozane

    @SerializedName("HourlyRentalPrice")
    var hourlyRentalPrice:Int?=0;// force
    @SerializedName("MonthlyRentalPrice")
    var monthlyRentalPrice:Int?=0;// force
    @SerializedName("YearlyRentPrice")
    var yearlyRentPrice:Int?=0;// force

    @SerializedName("ParkingTypeID")
    var parkingTypeID:Int?=0;
    @SerializedName("HeatingID")
    var heatingID:Int?=0;
    @SerializedName("CoolingID")
    var coolingID:Int?=0;
    @SerializedName("MeterOfHouse")
    var meterOfHouse:Int?=0;
    @SerializedName("Rooms")
    var rooms:Int?=0;
    @SerializedName("Bathrooms")
    var bathrooms:Int?=0;
    @SerializedName("YearOfHouse")
    var yearOfHouse:Int?=0;

    @SerializedName("FuelType")
    var fuelType:String?=null
    @SerializedName("YearOfCar")
    var yearOfCar:Int?=0
    @SerializedName("KilometerOfCar")
    var kilometerOfCar:Int?=0

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        agreement = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
        cityID = parcel.readString()
        categoryMarketID = parcel.readString()
        priceAgree = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        free = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        commodityPrice = parcel.readValue(Long::class.java.classLoader) as? Long
        subCategoryMarketID = parcel.readString()
        typeOfguaranteeTO = parcel.readParcelable(TypeOfguaranteeTO::class.java.classLoader)
        type = parcel.readString()
        salePrice = parcel.readValue(Int::class.java.classLoader) as? Long
        rentPrice = parcel.readValue(Int::class.java.classLoader) as? Int
        hourlyRentalPrice = parcel.readValue(Int::class.java.classLoader) as? Int
        monthlyRentalPrice = parcel.readValue(Int::class.java.classLoader) as? Int
        yearlyRentPrice = parcel.readValue(Int::class.java.classLoader) as? Int
        parkingTypeID = parcel.readValue(Int::class.java.classLoader) as? Int
        heatingID = parcel.readValue(Int::class.java.classLoader) as? Int
        coolingID = parcel.readValue(Int::class.java.classLoader) as? Int
        meterOfHouse = parcel.readValue(Int::class.java.classLoader) as? Int
        rooms = parcel.readValue(Int::class.java.classLoader) as? Int
        bathrooms = parcel.readValue(Int::class.java.classLoader) as? Int
        yearOfHouse = parcel.readValue(Int::class.java.classLoader) as? Int
        fuelType = parcel.readString()
        yearOfCar = parcel.readValue(Int::class.java.classLoader) as? Int
        kilometerOfCar = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(agreement)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(cityID)
        parcel.writeString(categoryMarketID)
        parcel.writeValue(priceAgree)
        parcel.writeValue(free)
        parcel.writeValue(commodityPrice)
        parcel.writeString(subCategoryMarketID)
        parcel.writeParcelable(typeOfguaranteeTO, flags)
        parcel.writeString(type)
        parcel.writeValue(salePrice)
        parcel.writeValue(rentPrice)
        parcel.writeValue(hourlyRentalPrice)
        parcel.writeValue(monthlyRentalPrice)
        parcel.writeValue(yearlyRentPrice)
        parcel.writeValue(parkingTypeID)
        parcel.writeValue(heatingID)
        parcel.writeValue(coolingID)
        parcel.writeValue(meterOfHouse)
        parcel.writeValue(rooms)
        parcel.writeValue(bathrooms)
        parcel.writeValue(yearOfHouse)
        parcel.writeString(fuelType)
        parcel.writeValue(yearOfCar)
        parcel.writeValue(kilometerOfCar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdMarketTO> {
        override fun createFromParcel(parcel: Parcel): AdMarketTO {
            return AdMarketTO(parcel)
        }

        override fun newArray(size: Int): Array<AdMarketTO?> {
            return arrayOfNulls(size)
        }
    }


}
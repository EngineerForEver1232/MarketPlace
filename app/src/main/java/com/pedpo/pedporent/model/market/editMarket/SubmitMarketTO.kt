package com.pedpo.pedporent.model.market.editMarket

import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.google.gson.annotations.SerializedName

//class SubmitMarketTO : AdMarketTO() {
class SubmitMarketTO  {

    @SerializedName("MarketID")
    var marketID:String?=null;// force
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
    @SerializedName("CategoryMarketID")
    var categoryMarketID:String?=null;// force
    @SerializedName("SubCategoryMarketID")
    var subCategoryMarketID:String?=null;// force
    @SerializedName("RentPrice")
    var rentPrice:String?=null;// force
    @SerializedName("SalePrice")
    var salePrice:Long?=null;// force
    @SerializedName("PriceAgree")
    var priceAgree:Boolean?=null;// force
    @SerializedName("Free")
    var free:Boolean?=null;// force

    @SerializedName("CommodityPrice")
    var commodityPrice:String?=null;// gheymat kala // force
    @SerializedName("typeOfguarantee")
    var typeOfguaranteeTO: TypeOfguaranteeTO?=null;// force
    @SerializedName("CityID")
    var cityID:String?=null;// force

    @SerializedName("ShowType")
    var showType:String?=null
    @SerializedName("HourlyRentalPrice")
    var hourlyRentalPrice:Long?=null
    @SerializedName("MonthlyRentalPrice")
    var monthlyRentalPrice:Long?=null
    @SerializedName("YearlyRentPrice")
    var yearlyRentPrice:Long?=null
    @SerializedName("Type")
    var type:String?=null


    @SerializedName("MeterOfHouse")
    var meterOfHouse:Int?=null
    @SerializedName("Rooms")
    var rooms:Int?=null
    @SerializedName("Bathrooms")
    var bathrooms:Int?=null
    @SerializedName("YearOfHouse")
    var yearOfHouse:Int?=null
    @SerializedName("ParkingTypeID")
    var parkingID:Int?=null
    @SerializedName("HeatingID")
    var heatingID:Int?=null
    @SerializedName("CoolingID")
    var coolingID:Int?=null

    @SerializedName("FuelType")
    var fuelType:Int?=null
    @SerializedName("YearOfCar")
    var yearOfCar:Int?=null
    @SerializedName("KilometerOfCar")
    var kilometerOfCar:Int?=null


    @SerializedName("DeleteURL")
    var deleteURL:List<String>?=null;
    @SerializedName("PhotoMarkets")
    var photoMarkets:List<PhotoMarkets>?=null;


}
package com.pedpo.pedporent.model.details

import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.google.gson.annotations.SerializedName

class DetailsTO {
    var marketID:String?=null;
    var title:String?=null;
    var description:String?=null;
    var agreement:String?=null;

    var commodityPrice:Long?=null;
    var scoreLike:Int?=null;
    var scoreView:Int?=null;
    var latitude:String?=null;
    var longitude:String?=null;
    var categoryName:String?=null;
    var subCategoryName:String?=null;
    var shareLink:String?=null;

    var storeID:String?=null;
    var type:String?=null;
    var neighborImage:String?=null;
    var rentPrice:Long?=null;
    var salePrice:Long?=null;

    var free:Boolean?=null;
    var priceAgree:Boolean?=null;

    var showType:String?=null;
    var hourlyRentalPrice:Long?=null;
    var monthlyRentalPrice:Long?=null;
    var yearlyRentPrice:Long?=null;

    var meterOfHouse:Int?=null;
    var rooms:Int?=null;
    var bathrooms:Int?=null;
    var yearOfHouse:Int?=null;
    @SerializedName("parkingTypeName")
    var parkingTypeName:String?=null;
    @SerializedName("heatingName")
    var heatingName:String?=null;
    var coolingName:String?=null;

    var isActive:Boolean?=false;
    var isCar:Boolean?=null;
    var isHome:Boolean?=null;
    var nameSite:String?=null;


    var urlLink:String?=null;
    var fuelType:String?=null;
    var yearOfCar:Int?=null;
    var kilometerOfCar:Int?=null;



    var cityName:String?=null;
    var provinceName:String?=null;
    var typeOfguarantee:TypeOfguaranteeTO?=null;
    var renterUserID:String?=null;

    var startTimeInactive:String?=null;
    var endTimeInactive:String?=null;

    var renterProfilePhoto:String?=null;
    var firstName:String?=null;
    var lastName:String?=null;
    var isBookMarkByUser:Boolean?=null;
    var isLikeByIp:Boolean?=null;


}
package com.pedpo.pedporent.model.filter

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.utills.ContextApp
import java.io.Serializable

class FilterTransfer : Serializable {

    var title: String?=null;
    var cityID: String?=null;
    var categoryID: String?=null;
    var categoryName: String?=null;
    var subCategoryID: String?=null;
    var typePrice: String?=null;
    var priceFrom: Long?=0
    var priceTo: Long?=0
    var priceAgree: Boolean?=false
    var free: Boolean?=false
    var registerDate: String?=null
    var typeOFPrice: String?=null;// for renteral / hourly , monthly , yearly
    var iP: String?=null;

    // Home
    var meterOfHouseFrom:Int?=null;
    var meterOfHouseTo:Int?=null
    var rooms:Int?=null
    var bathrooms:Int?=null
    var yearOfHouseFrom:Int?=null
    var yearOfHouseTo:Int?=null
    var parkingTypeID: String? = null;


    //Car
//    var fuelType:String?=null;
    var yearOfCarFrom:Int?=null
    var yearOfCarTo:Int?=null
    var kilometerOfCarFrom:Int?=null
    var kilometerOfCarTo:Int?=null

    var typeAdvanced:String?=null;
    var isAdvanced:Boolean?=null;



    constructor()

    constructor(
        title: String?,
        cityID: String?,
        categoryID: String?,
        type: String?,
        priceFrom: Long?,
        priceTo: Long?,
        priceAgree: Boolean?,
        free: Boolean?,
        registerDate: String?,
        iP: String?
    ) {
        this.title = title
        this.cityID = cityID
        this.categoryID = categoryID
        this.typePrice = type
        this.priceFrom = priceFrom
        this.priceTo = priceTo
        this.priceAgree = priceAgree
        this.free = free
        this.registerDate = registerDate
        this.iP = iP
    }
}
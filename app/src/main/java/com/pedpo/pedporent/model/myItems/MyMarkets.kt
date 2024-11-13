package com.pedpo.pedporent.model.myItems

data class MyMarkets(
    var marketID: String? = null,
    var title: String? = null,
    var photoAddress: String? = null,
    var registerDate: String? = null,
    var type: String? = null,
    var showType: String? = null,
    var rentPriceDay: Long? = null,
    var isActive: Boolean? = false,
    var isConfirm: Boolean? = false,
    var isService: Boolean? = false,
    var notification: String? = null,
    var description:String?=null,
    var priceType: String? = null,
    var place: String? = null,
    var free: Boolean? = null,
    var priceAgree: Boolean? = null,
    var startDate: String? = null,
    var endDate: String? = null,

    var rooms: String? = null,
    var bathrooms: String? = null,
    var meterOfHouse: String? = null,
    var isHome: Boolean? = false,

    var isCar: Boolean? = false,
    var fuelType: String? = null,
    var yearOfCar: String? = null,
    var kilometerOfCar: String? = null

)
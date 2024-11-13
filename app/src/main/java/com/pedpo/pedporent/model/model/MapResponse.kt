package com.pedpo.pedporent.model.model

import com.pedpo.pedporent.model.ResponseTO

class MapResponse : ResponseTO() {

    var data: ArrayList<MapData>? = null

}

data class MapData(
    var marketID: String,
    var title: String,
    var description: String,
    var photoAddress: String,
    var registerDate: String,
    var showType: String,
    var rentPriceDay: Long,
    var free: Boolean,
    var priceAgree: Boolean,
    var isActive: Boolean,
    var isBookMarkByUser: Boolean,
    var isLikeByIp: Boolean,
    var isService: Boolean,
    var type: String,
    var priceType: String,
    var startTimeInactive: String,
    var endTimeInactive: String,

    var place: String,
    var latitude: Double,
    var longitude: Double,


    var rooms: String? = null,
    var bathrooms: String? = null,
    var meterOfHouse: String? = null,
    var isHome: Boolean? = false,

    var isCar: Boolean? = false,
    var fuelType: String? = null,
    var yearOfCar: String? = null,
    var kilometerOfCar: String? = null,


    )

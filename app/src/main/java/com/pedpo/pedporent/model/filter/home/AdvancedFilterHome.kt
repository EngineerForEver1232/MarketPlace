package com.pedpo.pedporent.model.filter.home

import com.google.gson.annotations.SerializedName

data class AdvancedFilterHome (

    @SerializedName("MeterOfHouseFrom")
    var meterOfHouseFrom:Int,
    @SerializedName("MeterOfHouseTo")
    var meterOfHouseTo:Int,
    @SerializedName("Rooms")
    var rooms:Int,
    @SerializedName("Bathrooms")
    var bathrooms:Int,

    @SerializedName("YearOfHouseFrom")
    var yearOfHouseFrom:Int,
    @SerializedName("YearOfHouseTo")
    var yearOfHouseTo:Int,

)
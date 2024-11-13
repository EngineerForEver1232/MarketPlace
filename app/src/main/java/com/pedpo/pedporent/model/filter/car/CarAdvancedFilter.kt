package com.pedpo.pedporent.model.filter.car

import com.google.gson.annotations.SerializedName

data class CarAdvancedFilter (

    @SerializedName("YearOfCarFrom")
    var yearOfCarFrom:Int,
    @SerializedName("YearOfCarTo")
    var yearOfCarTo:Int,
    @SerializedName("KilometerOfCarFrom")
    var kilometerOfCarFrom:Int,
    @SerializedName("KilometerOfCarTo")
    var kilometerOfCarTo:Int

    )
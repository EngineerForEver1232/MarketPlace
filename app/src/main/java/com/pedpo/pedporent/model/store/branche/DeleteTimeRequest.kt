package com.pedpo.pedporent.model.store.branche

import com.google.gson.annotations.SerializedName

data class DeleteTimeRequest (

    @SerializedName("WorkTimeID")
    var workTimeID:String,
    @SerializedName("DayNumber")
    var dayNumber:Int,
    @SerializedName("ShiftTime")
    var shiftTime:Int


)
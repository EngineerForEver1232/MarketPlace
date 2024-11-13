package com.pedpo.pedporent.model.store.branche.time

import com.google.gson.annotations.SerializedName

data class EnableWorkTime (

    @SerializedName("BranchID")
    var branchID:String?=null,
    @SerializedName("WorkTimeID")
    var workTimeID:String?=null,
    @SerializedName("DayNumber")
    var dayNumber:Int,
    @SerializedName("On")
    var enable:Boolean


)
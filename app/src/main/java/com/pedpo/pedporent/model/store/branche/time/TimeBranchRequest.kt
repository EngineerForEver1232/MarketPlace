package com.pedpo.pedporent.model.store.branche.time

import com.google.gson.annotations.SerializedName

data class TimeBranchRequest (

    @SerializedName("BranchID")
    private var branchID:String,
    @SerializedName("StartOrEnd")
    private var startOrEnd:String,
    @SerializedName("Time")
    private var time:String,
    @SerializedName("DayNumber")
    private var dayNumber:Int,
    @SerializedName("ShiftTime")
    private var shiftTime:Int,
    @SerializedName("On")
    private var on:Boolean

)
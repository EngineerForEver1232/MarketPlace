package com.pedpo.pedporent.model.store.branche.add

import com.google.gson.annotations.SerializedName

data class AddAddressBrancheTO (
    @SerializedName("BranchID")
     var branchID :String?=null,
    @SerializedName("Name")
     var name :String,
    @SerializedName("Address")
     var address :String,
    @SerializedName("Latitude")
     var latitude :String,
    @SerializedName("Longitude")
     var longitude :String


)
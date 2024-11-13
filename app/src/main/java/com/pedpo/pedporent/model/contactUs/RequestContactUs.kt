package com.pedpo.pedporent.model.contactUs

import com.google.gson.annotations.SerializedName

data class RequestContactUs(

    @SerializedName("Title")
    var title:String,
    @SerializedName("Description")
    var description:String
)
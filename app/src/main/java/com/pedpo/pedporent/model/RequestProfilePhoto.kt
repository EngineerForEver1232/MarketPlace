package com.pedpo.pedporent.model

import com.google.gson.annotations.SerializedName

data class RequestProfilePhoto(
    @SerializedName("LinkImage")
    var linkImage: String?,
    @SerializedName("Image")
    var image: String?,
    )
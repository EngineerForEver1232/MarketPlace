package com.pedpo.pedporent.model.poster

import com.google.gson.annotations.SerializedName

class PosterTO {

    @SerializedName("photo")
    var photo:String?=null;
    @SerializedName("title")
    var title:String?=null;
    @SerializedName("link")
    var link:String?=null;
}
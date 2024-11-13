package com.pedpo.pedporent.model.filter

import com.google.gson.annotations.SerializedName

data class TitleSearchFilter(

    @SerializedName("Title")
    var title:String ,
    @SerializedName("Ip")
    var ip:String ,
    @SerializedName("Paging")
//    @SerializedName("PagingParameters")
    var Paging: PagingNumber

)
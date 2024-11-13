package com.pedpo.pedporent.model.market.category

import com.google.gson.annotations.SerializedName

class Categories {

    @SerializedName("caegroymarket")
    var categoryMarket :List<CategoryTO>?=null

    @SerializedName("caegroyservice")
    var categoryService :List<CategoryTO>?=null

    var type:String?=null;


}
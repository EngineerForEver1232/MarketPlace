package com.pedpo.pedporent.model.market.category

import com.pedpo.pedporent.model.ResponseTO
import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.market.category.Categories

class CategoryData : ResponseTO(){

//    @SerializedName("data")
//    var data:List<CategoryTO>?=null;

    @SerializedName("data")
    var data:Categories?=null;

}
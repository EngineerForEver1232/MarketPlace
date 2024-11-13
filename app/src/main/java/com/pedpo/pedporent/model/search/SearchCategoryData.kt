package com.pedpo.pedporent.model.search

import com.pedpo.pedporent.model.ResponseTO
import com.google.gson.annotations.SerializedName

class SearchCategoryData :ResponseTO(){

    @SerializedName("data")
    var data:ArrayList<SearchCategoryTO>?=null;

}
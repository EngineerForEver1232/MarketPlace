package com.pedpo.pedporent.model.store.branche

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.ResponseTO

class BranchesData : ResponseTO(){

    var data:List<BranchesTO>?=null


   data class BranchesTO  (
        @SerializedName("branchID")
        var branchID: String?=null,
        @SerializedName("name")
        var name: String?=null

    )

}
package com.pedpo.pedporent.model.filter.home

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.filter.InputFilter
import com.pedpo.pedporent.model.filter.PagingNumber

data class RequestFilterAdvancedHome(

    @SerializedName("InputFilter")
    var inputFilter:InputFilter,
    @SerializedName("AdvanceFilter")
    var advancedFilterHome: AdvancedFilterHome,
    @SerializedName("Paging")
    var Paging: PagingNumber

)
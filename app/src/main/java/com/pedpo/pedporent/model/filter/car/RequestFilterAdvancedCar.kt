package com.pedpo.pedporent.model.filter.car

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.filter.InputFilter
import com.pedpo.pedporent.model.filter.PagingNumber

data class RequestFilterAdvancedCar(

    @SerializedName("InputFilter")
    var inputFilter:InputFilter,
    @SerializedName("AdvanceFilter")
    var carAdvancedFilter: CarAdvancedFilter,
    @SerializedName("Paging")
    var Paging: PagingNumber

)
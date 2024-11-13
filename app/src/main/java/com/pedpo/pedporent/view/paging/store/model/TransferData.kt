package com.pedpo.pedporent.view.paging.store.model

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO

data class TransferData(

    var title: String? = null,
    var storeID: String? = null,
    var cityID: String? = null,
    var categoryID: String? = null,
    var ip: String? = null,
    var typeAPI: String? = null

)
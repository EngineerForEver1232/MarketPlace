package com.pedpo.pedporent.model.renterMarket

import com.pedpo.pedporent.model.market.ShowMarketTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

data class RenterMarketTO(

    var firstName:String,
    var lastName:String,
    var image:String,
    var renterIsUser:Boolean,
    var listRenterMarketsViewModel:List<PaginTO>

)
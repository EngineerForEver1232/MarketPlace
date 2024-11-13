package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

interface OnClickStore {

    fun onClickStore(paginTO: PaginTO?,position:Int?,type:String?)

}
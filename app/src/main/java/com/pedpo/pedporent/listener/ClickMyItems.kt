package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.myItems.MyMarkets

interface ClickMyItems {

    fun onClickMyItems(market: MyMarkets?,position:Int?,type:String?)

}
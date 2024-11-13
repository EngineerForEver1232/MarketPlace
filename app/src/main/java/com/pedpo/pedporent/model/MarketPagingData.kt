package com.pedpo.pedporent.model

import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO


class MarketPagingData :ResponseTO(){

    var totalPage:Int?=null;
    var data:List<PaginTO>?=null;

}
package com.pedpo.pedporent.listener

import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

interface ClickAdapterPaging {

    fun onClickAdapterPaging(paginTO: PaginTO?,action:String,binding: AdapterPaginBinding)

}
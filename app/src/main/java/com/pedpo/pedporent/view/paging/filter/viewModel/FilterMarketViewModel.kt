package com.pedpo.pedporent.view.paging.filter.viewModel

import android.util.Log
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.PagingData
import com.pedpo.pedporent.api.FilterAPI

import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.filter.FilterTransfer
import com.pedpo.pedporent.view.paging.filter.dataSource.FilterMarketDataSource
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel.LastMarketViewModel
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterMarketViewModel : ViewModel{

    private var serviceAPI: FilterAPI?=null;
    private var filterTransfer:FilterTransfer?=null;
    val posts : Flow<PagingData<PaginTO>> = selectPosts();

    @Inject
    constructor(serviceAPI: FilterAPI){
        this.serviceAPI = serviceAPI;
    }

    fun initData(filterTransfer: FilterTransfer){
        this.filterTransfer = filterTransfer;
    }

    private fun selectPosts(): Flow<PagingData<PaginTO>> {
        Log.i("testFilter", "selectPosts viewModel: ")

        return Pager(
            config = PagingConfig(pageSize = LastMarketViewModel.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { FilterMarketDataSource(serviceAPI,filterTransfer) }
        ).flow.cachedIn(viewModelScope)
    }

    companion object {
        const val PAGE_SIZE = 1
    }

}
package com.pedpo.pedporent.view.paging.marketWithCategory.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.marketWithCategory.dataSource.CategoryMarketDataSource
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CategoryMarketViewModel : ViewModel{

    private var serviceAPI:ServiceAPI?=null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
    this.serviceAPI= serviceAPI;
    }

    private var content:String?=null;
    private var cityID:String?=null;
    private var ip:String?=null;
    fun initCotent(content:String?,ip:String?,cityID:String?){
        this.cityID = cityID;
        this.content = content;
        this.ip = ip;
    }

    val posts : Flow<PagingData<PaginTO>> = selectPosts();

    private fun selectPosts(): Flow<PagingData<PaginTO>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CategoryMarketDataSource(serviceAPI!!,content,ip,cityID) }
        ).flow.cachedIn(viewModelScope)
    }

    companion object {
        const val PAGE_SIZE = 1
    }
}
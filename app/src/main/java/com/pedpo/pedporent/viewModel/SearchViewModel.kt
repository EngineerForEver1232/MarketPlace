package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.MarketPagingData
import com.pedpo.pedporent.model.repository.SearchRepository
import com.pedpo.pedporent.model.search.SearchCategoryData
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel : ViewModel {

    private var searchRepository:SearchRepository?=null;

    @Inject
    constructor(searchRepository: SearchRepository) {
        this.searchRepository = searchRepository;
    }

    fun searchCategory(title:String):LiveData<DataWrapper<SearchCategoryData>>{
       return this.searchRepository?.searchCategory(title = title)!!
    }

    fun searchMarket(request: RequestSearchMarket):LiveData<DataWrapper<MarketPagingData>>{
        return this.searchRepository?.searchMarket(requestSearch = request)!!
    }


}
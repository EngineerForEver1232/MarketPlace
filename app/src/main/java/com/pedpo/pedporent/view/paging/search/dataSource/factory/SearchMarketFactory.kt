package com.pedpo.pedporent.view.paging.search.dataSource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.search.dataSource.SearchMarketDataSource
import javax.inject.Inject


class SearchMarketFactory : DataSource.Factory<Long, PaginTO> {

    private var dataSource: SearchMarketDataSource? = null;
    private var mutableDataSource: MutableLiveData<SearchMarketDataSource>? = null;
    private var serviceAPI: ServiceAPI? = null;
    var cityID: String? = null;
    var title: String? = null;
    var categoryID: String? = null;

        @Inject
    constructor() {
        this.mutableDataSource = MutableLiveData();
    }

    fun paramFactory(serviceAPI: ServiceAPI?,title: String?, categoryID: String?,cityID:String?) {
        this.title = title
        this.categoryID = categoryID
        this.serviceAPI = serviceAPI;
        this.cityID = cityID;
    }

    override fun create(): DataSource<Long, PaginTO> {
        dataSource = SearchMarketDataSource(serviceAPI = serviceAPI, title, categoryID, cityID = cityID)
        mutableDataSource?.postValue(dataSource)
        return dataSource!!
    }





    fun getMutableDataSource(): MutableLiveData<SearchMarketDataSource> {
        return mutableDataSource!!
    }


}
package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.dataSource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.dataSource.VisitedMarketDataSource
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import javax.inject.Inject

class VisitedMarketDataFactory :DataSource.Factory<Long, PaginTO>{

    private var mutableDataSourceVisited:MutableLiveData<VisitedMarketDataSource>?=null;
    private var dataSourceVisited:VisitedMarketDataSource?=null;

    private var serviceAPI: ServiceAPI? = null;
    var title: String? = null;
    var cityID: String? = null;
    var categoryID: String? = null;
    var ip: String? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
//        this.serviceAPI = serviceAPI;
        this.mutableDataSourceVisited = MutableLiveData<VisitedMarketDataSource>();
//        this.dataSource = marketDataSource;

    }

    fun paramFactory(serviceAPI: ServiceAPI?,title: String?, categoryID: String?,ip:String?,cityID:String?) {
        this.cityID = cityID
        this.title = title
        this.categoryID = categoryID
        this.serviceAPI = serviceAPI;
        this.ip = ip;
    }

    override fun create(): DataSource<Long,PaginTO> {
        dataSourceVisited = VisitedMarketDataSource(serviceAPI = serviceAPI, title, categoryID,ip = ip,cityID)
//        dataSource = MarketDataSource(serviceAPI!!);
        mutableDataSourceVisited?.postValue(dataSourceVisited);
        return dataSourceVisited!!;
    }



    fun getMutableDataSource():MutableLiveData<VisitedMarketDataSource>{
        return mutableDataSourceVisited!!;
    }


}
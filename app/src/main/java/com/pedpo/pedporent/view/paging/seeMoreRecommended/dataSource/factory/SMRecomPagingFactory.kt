package com.pedpo.pedporent.view.paging.seeMoreRecommended.dataSource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreRecommended.dataSource.SMRecomPagingDataSource
import javax.inject.Inject

class SMRecomPagingFactory : DataSource.Factory<Long, PaginTO> {

    private var dataSource: SMRecomPagingDataSource? = null;
    private var mutableDataSource: MutableLiveData<SMRecomPagingDataSource>? = null;
    private var serviceAPI:ServiceAPI?=null;
    private var prefApp:PrefApp?=null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
        this.mutableDataSource = MutableLiveData();
        this.prefApp = prefApp;
    }

    override fun create(): DataSource<Long, PaginTO> {
        dataSource = SMRecomPagingDataSource(serviceAPI!!, prefApp = prefApp)
        mutableDataSource?.postValue(dataSource)
        return dataSource!!;
    }

    fun getMutableDataSource(): MutableLiveData<SMRecomPagingDataSource> {
        return mutableDataSource!!;
    }

}
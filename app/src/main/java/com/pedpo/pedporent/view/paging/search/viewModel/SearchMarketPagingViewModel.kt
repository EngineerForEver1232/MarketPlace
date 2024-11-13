package com.pedpo.pedporent.view.paging.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.PagedList
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import com.pedpo.pedporent.view.paging.search.dataSource.factory.SearchMarketFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO


@HiltViewModel
class SearchMarketPagingViewModel :ViewModel {

    private var factory:SearchMarketFactory?=null;
    private var executor:Executor?=null;
    var netWorkState:LiveData<NetworkState>?=null;
    var filterTextAll = MutableLiveData<RequestSearchMarket>();
    var serviceAPI:ServiceAPI?=null;

    var pagedList:LiveData<PagedList<PaginTO>>?=null;
    private var prefApp: PrefApp

    @Inject
    constructor(serviceAPI: ServiceAPI?,factory: SearchMarketFactory,prefApp: PrefApp){
        this.factory = factory;
        this.serviceAPI = serviceAPI;
        this.prefApp = prefApp;
        init()
    }

    fun init(){

        pagedList =Transformations.switchMap(filterTextAll){
            this.factory?.paramFactory(serviceAPI,it.title,it.categoryID,prefApp.getCityID())

            var config1 = Config(
                enablePlaceholders = false,
                pageSize = 3,
                initialLoadSizeHint = 3
            )

            (LivePagedListBuilder(factory!!, config = config1))
                .setFetchExecutor(executor!!)
                .build()
        };

        executor = Executors.newFixedThreadPool(5);
        netWorkState = Transformations.switchMap(factory?.getMutableDataSource()!!){
                dataSource -> dataSource.networkState;
        }
//        this.factory?.paramFactory(serviceAPI,title,categoryID)
//
//        executor = Executors.newFixedThreadPool(5);
//
//        netWorkState = Transformations.switchMap(factory?.getMutableDataSource()!!){
//            dataSource -> dataSource.networkState
//        }
//
//        var config = Config(
//            enablePlaceholders = false,
//            pageSize = 3,
//            initialLoadSizeHint = 3
//        )
//
//        pagedList = LivePagedListBuilder(factory!!, config = config)
//            .setFetchExecutor(executor!!)
//            .build()

    }

    fun invalidateDataSource() {
        factory?.getMutableDataSource()?.value?.invalidate()
    }

}
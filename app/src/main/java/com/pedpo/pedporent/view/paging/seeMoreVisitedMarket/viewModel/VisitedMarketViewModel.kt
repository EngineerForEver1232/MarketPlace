package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.dataSource.factory.VisitedMarketDataFactory
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class VisitedMarketViewModel : ViewModel {

    private var executor: Executor? = null;
    var networkState: LiveData<NetworkState?>? = null;
    var pagedList: LiveData<PagedList<PaginTO>>? = null;
    var factoryVisited: VisitedMarketDataFactory? = null;
    var filterTextAll = MutableLiveData<RequestSearchMarket>();
    var serviceAPI:ServiceAPI?=null;



    @Inject
    constructor(serviceAPI: ServiceAPI?, visitedMarketDataFactory: VisitedMarketDataFactory) {
        this.factoryVisited = visitedMarketDataFactory;
        this.serviceAPI = serviceAPI;
        init()
    }

    private fun init() {
//        executor = Executors.newFixedThreadPool(5)
//        networkState = Transformations.switchMap(factory?.getMutableDataSource()!!)
//        {
//                dataSource -> dataSource.networkState
//        }
//
//        val pagedListConfig = Config(
//            pageSize = 3,
//            enablePlaceholders = false,
//            initialLoadSizeHint = 1
//        )
//
//        pagedList = LivePagedListBuilder(factory!!, pagedListConfig)
//            .setFetchExecutor(executor!!)
//            .build()


        pagedList =Transformations.switchMap(filterTextAll){
            this.factoryVisited?.paramFactory(serviceAPI,it.title,it.categoryID,it.ip,it.cityID)

            var config1 = Config(
                enablePlaceholders = false,
                pageSize = 3,
                initialLoadSizeHint = 3
            )

            (LivePagedListBuilder(factoryVisited!!, config = config1))
                .setFetchExecutor(executor!!)
                .build()
        };

        executor = Executors.newFixedThreadPool(5);
        networkState = Transformations.switchMap(factoryVisited?.getMutableDataSource()!!){
                dataSource -> dataSource.networkState;
        }

    }

    fun invalidateDataSource() {
//        marketDataFactory?.create()?.invalidate()
        factoryVisited?.getMutableDataSource()?.value?.invalidate()
    }

}
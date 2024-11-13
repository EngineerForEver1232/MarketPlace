package com.pedpo.pedporent.view.paging.seeMoreRecommended.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import com.pedpo.pedporent.view.paging.seeMoreRecommended.dataSource.factory.SMRecomPagingFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class SMRecomViewModel : ViewModel {

    private var factory: SMRecomPagingFactory? = null;
    private var executer:Executor?=null;
    var networkState:LiveData<NetworkState>?=null;
    var pagedListSMRecom:LiveData<PagedList<PaginTO>>?=null;


    @Inject
    constructor(smRecomPagingFactory: SMRecomPagingFactory) {
        this.factory = smRecomPagingFactory;
        init();
    }

     fun init() {

        executer = Executors.newFixedThreadPool(5)
        networkState = Transformations.switchMap(factory?.getMutableDataSource()!!){
            dataSource->dataSource.networkState
        }

        val pagedListConfig = Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(3)
            .setPageSize(3).build()

        pagedListSMRecom = LivePagedListBuilder(factory!!,pagedListConfig)
            .setFetchExecutor(executer!!)
            .build()
    }

    fun invalidateDataSource() {
        factory?.getMutableDataSource()?.value?.invalidate()
    }
}
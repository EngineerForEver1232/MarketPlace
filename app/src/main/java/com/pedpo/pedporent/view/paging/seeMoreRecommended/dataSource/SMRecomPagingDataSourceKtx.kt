package com.pedpo.pedporent.view.paging.seeMoreRecommended.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import kotlinx.coroutines.*
import javax.inject.Inject

class SMRecomPagingDataSourceKtx : ItemKeyedDataSource<Long, PaginTO> {

    private val TAG = "testPaging"

    var networkState:MutableLiveData<NetworkState>?=null;
    var initialLoading:MutableLiveData<NetworkState>?=null;

    var serviceAPI:ServiceAPI?=null;

    @Inject
    constructor(serviceAPI: ServiceAPI){
        this.serviceAPI = serviceAPI;
        networkState = MutableLiveData();
        initialLoading = MutableLiveData();
    }

    override fun getKey(item: PaginTO): Long {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<PaginTO>,
    ) {

        initialLoading?.postValue(NetworkState.LOADING)
        networkState?.postValue(NetworkState.LOADING)

        var requestPagingTO = RequestPaginTO((1).toString(), params.requestedLoadSize.toString());
        var requestPagingData = RequestPagingData(
            requestPagingTO,
            "f185d1c7-9f62-49ff-aa58-84a5d6ad9511",
            "79498657-15e9-4095-8b22-3756074f2827",null
        );

        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, "onFailure  " + t.message)
            val errorMessage = if (t == null) "unknown error" else t.message!!;
            networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage));
        };

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceAPI?.seeMoreRecommendMarkets(requestPagingData = requestPagingData)

            withContext(Dispatchers.Main){

                if (response?.isSuccessful!!){
                    callback.onResult(response.body()?.data!!)

                    initialLoading?.postValue(NetworkState.LOADED)
                    networkState?.postValue(NetworkState.LOADED)
                }else{

                    initialLoading?.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))
                    networkState?.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))

                }

            }

        }


    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<PaginTO>) {
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<PaginTO>) {


        networkState?.postValue(NetworkState.LOADING)

        var requestPagingTO =
            RequestPaginTO(params.key.toString(), params.requestedLoadSize.toString());
        var requestPagingData = RequestPagingData(
            requestPagingTO,
            "f185d1c7-9f62-49ff-aa58-84a5d6ad9511",
            "79498657-15e9-4095-8b22-3756074f2827",
            null
        );

        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, "onFailure  " + t.message)
            val errorMessage = if (t == null) "unknown error" else t.message!!;
            networkState?.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage));
        };

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceAPI?.seeMoreRecommendMarkets(requestPagingData = requestPagingData)
            Log.i(
                TAG,
                "Loading Rang " + params.key + " Count " + params.requestedLoadSize + " " + response?.body()?.data?.size
            )
            withContext(Dispatchers.Main){

                if (response?.isSuccessful!!){

                    var nextKey = if (response.body()?.totalPage?.toLong() == params.key) null else params.key+1
                    callback.onResult(response?.body()?.data!!)

                    networkState?.postValue(NetworkState.LOADED)

                }else{
                    networkState?.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                }
            }
        }

    }
}

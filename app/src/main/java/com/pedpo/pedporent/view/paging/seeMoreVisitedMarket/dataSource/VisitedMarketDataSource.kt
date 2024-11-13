package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import kotlinx.coroutines.*

class VisitedMarketDataSource : PageKeyedDataSource<Long, PaginTO> {

    private val TAG = "testPaging"

    var networkState: MutableLiveData<NetworkState?>? = null
    var initialLoading: MutableLiveData<NetworkState?>? = null

    var serviceAPI: ServiceAPI? = null;

    var title: String? = null;
    var cityID: String? = null;
    var categoryID: String? = null;
    var ip: String? = null;

    constructor(serviceAPI: ServiceAPI?, title: String?, categoryID: String?,ip:String?,cityID:String?) {

        this.cityID = cityID;
        this.ip = ip;
        this.title = title;
        this.categoryID = categoryID;

        this.serviceAPI = serviceAPI;

        networkState = MutableLiveData()
        initialLoading = MutableLiveData()
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, PaginTO>,
    ) {
        initialLoading?.postValue(NetworkState.LOADING)
        networkState?.postValue(NetworkState.LOADING)
        Log.i(TAG, "loadInitial $ip")


        var requestPagingTO = RequestPaginTO((1).toString(), params.requestedLoadSize.toString());
        var requestPagingData = RequestPagingData(
            requestPagingTO,
            cityID = cityID,
            null,
            ip =  ip
        );

        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            val errorMessage = if (t == null) "unknown error" else t.message!!;
            networkState?.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage));
        };

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response =
                serviceAPI?.seeMoreMostVisitedMarkets(requestPagingData = requestPagingData);

//            Log.i(
//                TAG,
//                "loadInitial " + response?.isSuccessful + " " + response?.message() + " " + response?.code()
//            )

            withContext(Dispatchers.Main){

                if (response?.isSuccessful!!) {
//                    callback.onResult(response.body()?.data!!, null, 2L)
                    callback.onResult(response.body()?.data!!, null, 2L)

                    initialLoading?.postValue(NetworkState.LOADED)
                    networkState?.postValue(NetworkState.LOADED)
                } else {
                    initialLoading?.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                    networkState?.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                }
            }

        }

    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, PaginTO>) {

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, PaginTO>) {

        networkState?.postValue(NetworkState.LOADING)


        var requestPagingTO =
            RequestPaginTO(params.key.toString(), params.requestedLoadSize.toString());
        var requestPagingData = RequestPagingData(
            requestPagingTO,
            cityID = cityID,
            null,
            ip = ip
        );


        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, "onFailure  " + t.message)
            val errorMessage = if (t == null) "unknown error" else t.message!!;
            networkState?.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage));
        };

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response =
                serviceAPI?.seeMoreMostVisitedMarkets(requestPagingData = requestPagingData);

            Log.i(
                TAG,
                "Loading Rang " + params.key + " Count " + params.requestedLoadSize + " " + response?.body()?.data?.size
            )

            withContext(Dispatchers.Main){
                if (response?.isSuccessful!!) {
                    var nextKey =
                        if (response.body()?.totalPage?.toLong() == params.key) null else params.key+1

                    callback.onResult(response.body()?.data!!, nextKey)

                    networkState?.postValue(NetworkState.LOADED)
                } else {
                    networkState?.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response.message()
                        )
                    )
                }
            }

        }

    }

}
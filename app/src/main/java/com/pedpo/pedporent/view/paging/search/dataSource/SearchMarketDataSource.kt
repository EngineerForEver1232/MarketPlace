package com.pedpo.pedporent.view.paging.search.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.di.utill.ApplicationPedpo
import com.pedpo.pedporent.model.search.market.PagingSearch
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import kotlinx.coroutines.*

class SearchMarketDataSource : PageKeyedDataSource<Long, PaginTO> {

    private val TAG = "testSearch"


    var networkState: MutableLiveData<NetworkState>? = null;
    var initialLoading: MutableLiveData<NetworkState>? = null;

    var serviceAPI: ServiceAPI? = null;

    var title: String? = null;
    var cityID: String? = null;
    var categoryID: String? = null;

    constructor(serviceAPI: ServiceAPI?, title: String?, categoryID: String?, cityID: String?) {
        this.cityID = cityID;
        this.title = title;
        this.categoryID = categoryID;

        this.serviceAPI = serviceAPI;

        networkState = MutableLiveData();
        initialLoading = MutableLiveData();
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, PaginTO>,
    ) {

        Log.i(TAG, "loadInitial: $title category  $categoryID city = $cityID")

        initialLoading?.postValue(NetworkState.LOADING)
        networkState?.postValue(NetworkState.LOADING)

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "exceptionHandler: ${throwable.message}")
            val errorMessage = if (throwable == null) "unknown error" else throwable.message!!;
            networkState?.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
        }

        var request = RequestSearchMarket(
            title = title, categoryID = categoryID,
            cityID = cityID, ip = GettingIP(ApplicationPedpo.context).deviceIpAddress,
//            title,categoryID,BaseConstants.CITY_ID,
            paging = PagingSearch("1", "10")
        );

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.searchMarket(request);

            withContext(Dispatchers.Main) {
                Log.i(TAG, "loadInitialll: ${response?.isSuccessful}${response?.message()}")

                if (response?.isSuccessful == true) {

                    callback.onResult(
                        response.body()?.data ?: ArrayList<PaginTO>(), null, 2L)


                    initialLoading?.postValue(NetworkState.LOADED)
                    networkState?.postValue(NetworkState.LOADED)
                } else {

                    initialLoading?.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response?.message()
                        )
                    )
                    networkState?.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response?.message()
                        )
                    )
                }

            }

        }

    }


    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, PaginTO>) {

    }


    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, PaginTO>) {

        networkState?.postValue(NetworkState.LOADING)
        var pagintSearch =
            PagingSearch(params.key.toString(), params.requestedLoadSize.toString());
        var request = RequestSearchMarket(
            title = title, categoryID = categoryID,
            cityID = cityID, ip = GettingIP(ApplicationPedpo.context).deviceIpAddress,
//            title,categoryID,BaseConstants.CITY_ID,
            paging = pagintSearch
        );

        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, "onFailure  " + t.message)
            val errorMessage = if (t == null) "unknown error" else t.message!!;
            networkState?.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage));
        };


        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.searchMarket(request);

            withContext(Dispatchers.Main) {

                if (response?.isSuccessful == true) {

                    var nextKey =
                        if (response.body()?.totalPage?.toLong() == params.key) null else params.key + 1
                    callback.onResult(response?.body()?.data ?: ArrayList<PaginTO>(), nextKey)

                    networkState?.postValue(NetworkState.LOADED)


                } else {
                    networkState?.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response?.message()
                        )
                    )
                }

            }

        }


    }


}
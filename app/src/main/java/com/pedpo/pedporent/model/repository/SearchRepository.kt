package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.MarketPagingData
import com.pedpo.pedporent.model.search.RequestSearch
import com.pedpo.pedporent.model.search.SearchCategoryData
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.utills.permission.PrefApp
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchRepository {

    private var serviceAPI: ServiceAPI? = null;
    private var prefApp:PrefApp?=null

    @Inject
    constructor(serviceAPI: ServiceAPI,prefApp: PrefApp) {
        this.serviceAPI = serviceAPI;
        this.prefApp = prefApp;
    }

    fun searchCategory(title: String): MutableLiveData<DataWrapper<SearchCategoryData>> {

        var mutable = MutableLiveData<DataWrapper<SearchCategoryData>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testApi", "exceptionHandler: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.searchCategory(RequestSearch(title = title , prefApp?.getCityID()?:""  ))
            Log.i("testApi", "onSuccess1: ${response?.isSuccessful}")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    Log.i("testApi", "onSuccess2: ${response?.isSuccessful}")
                    Log.i("testApi", "onSuccess2: ${response?.body()}")
                    mutable.value=DataWrapper(null, response?.body())
                } else {
//                    Log.i("testApi", "false: ${response?.message()!!.toString()}")
                    mutable.value = DataWrapper(Exception(response.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun searchMarket(requestSearch: RequestSearchMarket):MutableLiveData<DataWrapper<MarketPagingData>>{

        var mutable = MutableLiveData<DataWrapper<MarketPagingData>>()

        var exceptionHanlder = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHanlder).launch{

            var response = serviceAPI?.searchMarket(requestSearchMarket = requestSearch)

            withContext(Dispatchers.Main){

                if (response?.isSuccessful!!){
                    mutable.value = DataWrapper(null,response.body())
                }else
                    mutable.value = DataWrapper(Exception(response.body()?.message),null)
            }

        }
        return mutable;
    }


}
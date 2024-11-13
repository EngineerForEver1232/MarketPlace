package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.MapApi
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.model.MapResponse
import com.pedpo.pedporent.model.model.OneMarketMap
import com.pedpo.pedporent.model.model.RequestMapTO
import com.pedpo.pedporent.utills.ContextApp
import kotlinx.coroutines.*
import javax.inject.Inject

class MapRepository @Inject constructor(private val mapApi: MapApi) {

    fun market(requestMapTO: RequestMapTO?):MutableLiveData<DataWrapper<MapResponse>>{
        val mutable = MutableLiveData<DataWrapper<MapResponse>>()

        val exceptionHandler = CoroutineExceptionHandler { _,throwable ->
            Log.e("mapTest", "market: ${throwable.message}" )
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = if (requestMapTO?.type == ContextApp.RENT || requestMapTO?.type == ContextApp.SALE)
                mapApi.markets(requestMapTO = requestMapTO)
            else
                mapApi.services(requestMapTO = requestMapTO)

//            val response = mapApi.markets(requestMapTO = requestMapTO)
            Log.i("mapTest", "market: ${response.isSuccessful}" )
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value  = DataWrapper(Exception(response.body()?.message),null)
                }
            }
        }
        return mutable;
    }

    fun onMarket(cityID:String):MutableLiveData<DataWrapper<OneMarketMap>>{
        val mutable = MutableLiveData<DataWrapper<OneMarketMap>>()

        val exceptionHandler = CoroutineExceptionHandler { _,throwable ->
            Log.e("mapTest", "market: ${throwable.message}" )
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = mapApi.oneMarkets(cityID = cityID)
            Log.i("mapTest", "market: ${response.isSuccessful}" )
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value  = DataWrapper(Exception(response.body()?.message),null)
                }
            }
        }
        return mutable;
    }

}
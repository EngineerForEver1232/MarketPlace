package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.R
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.Poster
import com.pedpo.pedporent.model.market.ShowMarketTO
import com.pedpo.pedporent.model.market.VisitMarketData
import com.pedpo.pedporent.model.poster.PosterData
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import javax.inject.Inject


class HomeRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }

    fun getPoster(): MutableLiveData<List<Poster>> {
        val mutableLiveData = MutableLiveData<List<Poster>>();

        var list = ArrayList<Poster>();
        list.add(Poster(R.drawable.motor1))
        list.add(Poster(R.drawable.motor2))
        list.add(Poster(R.drawable.motor3))
        list.add(Poster(R.drawable.motor4))
        list.add(Poster(R.drawable.motor5))
        list.add(Poster(R.drawable.motor6))

        mutableLiveData.value = list;
        return mutableLiveData;
    }




    fun getMarketPedpo(): MutableLiveData<DataWrapper<JsonObject>> {

        var mutableLiveData = MutableLiveData<DataWrapper<JsonObject>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutableLiveData.postValue(
                DataWrapper<JsonObject>(
                    Exception(throwable.localizedMessage),
                    null!!
                )
            );
//            Log.e("testRetrofit", "Exception handled: ${throwable.localizedMessage}")
        }

        val viewModelJob = SupervisorJob()
        CoroutineScope(Dispatchers.IO + exceptionHandler)
            .launch {

                var response = serviceAPI?.getMarketsCotourines()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful!!) {
                        Log.i(
                            "testRetrofit", "Repository onSuccess: " +
                                    response.body()
                        )
                        mutableLiveData.postValue(DataWrapper<JsonObject>(null, response.body()!!));
                    } else {
                        Log.e("testRetrofit", "Repository onError: ")
                        mutableLiveData.postValue(
                            DataWrapper<JsonObject>(
                                Exception(response?.message()),
                                null!!
                            )
                        );
                    }
                }
            }

        return mutableLiveData;
    }


    fun poster():MutableLiveData<DataWrapper<PosterData>>{

        val mutable = MutableLiveData<DataWrapper<PosterData>>();

        var exceptionHandler = CoroutineExceptionHandler {_,throwable->
            Log.e("testPoster", "poster: ${throwable.localizedMessage}" )
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = serviceAPI?.poster();
            withContext(Dispatchers.Main){
                Log.i("testPoster", "poster: ${response?.body()?.message}" )

                if (response?.isSuccessful==true){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value  = DataWrapper(Exception(response?.body()?.message),null)
                }
            }
        }

        return mutable;
    }

    fun lastMarkets(showMarketTO: ShowMarketTO): MutableLiveData<DataWrapper<VisitMarketData>> {
        Log.i("showMarkets", "last")
        var mutable = MutableLiveData<DataWrapper<VisitMarketData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("showMarkets", "exceptionHandler: "+throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.lastMarkets(showMarketTO = showMarketTO)

            withContext(Dispatchers.Main) {
                Log.i("showMarkets", "respositoy: "+response?.code())
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    Log.i("showMarkets", "respositoy: "+response.code())
                    mutable.value = DataWrapper(Exception(response.message()), null)
                }
            }
        }
        return mutable;
    }

    fun recommendedMarkets(showMarketTO: ShowMarketTO): MutableLiveData<DataWrapper<VisitMarketData>> {
        var mutable = MutableLiveData<DataWrapper<VisitMarketData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("showMarkets", "exceptionHandler: "+throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.recommendedMarkets(showMarketTO = showMarketTO)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    Log.e("showMarkets", "respositoy: "+response.code())
                    mutable.value = DataWrapper(Exception(response.message()), null)
                }
            }
        }
        return mutable;
    }
    fun mostViewMarkets(showMarketTO: ShowMarketTO): MutableLiveData<DataWrapper<VisitMarketData>> {
        var mutable = MutableLiveData<DataWrapper<VisitMarketData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("showMarkets", "exceptionHandler: "+throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.mostViewMarkets(showMarketTO = showMarketTO)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    Log.e("showMarkets", "respositoy: "+response.code())
                    mutable.value = DataWrapper(Exception(response.message()), null)
                }
            }
        }
        return mutable;
    }

}
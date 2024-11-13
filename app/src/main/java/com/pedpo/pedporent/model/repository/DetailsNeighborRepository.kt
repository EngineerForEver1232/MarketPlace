package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailsData
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class DetailsNeighborRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }

    fun detailsMarkets(viewTO: ViewTO): MutableLiveData<DataWrapper<DetailsData>> {
        var mutable = MutableLiveData<DataWrapper<DetailsData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("detailsNeighbor", "CoroutineExceptionHandler: ${throwable.localizedMessage}" )

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

//            val response = serviceAPI?.detailsMarket(viewTO);
            val response = serviceAPI?.detailNeighborMarket(viewTO.neighborMarketID);

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else
                    mutable.value = DataWrapper(Exception(response.message()), null)
            }
        }

        return mutable;
    }

    fun photoDetails(id: String): MutableLiveData<DataWrapper<PhotoDetailsData>> {
        val mutable = MutableLiveData<DataWrapper<PhotoDetailsData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceAPI?.detailsPhotos(id,"");

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response.message()), null)
                }
            }
        }

        return mutable;
    }


    fun checkRenter(userID:String): MutableLiveData<DataWrapper<RenterData>> {
        var mutable = MutableLiveData<DataWrapper<RenterData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.checkRenter(userID = userID)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null)
                }
            }

        }
        return mutable;
    }

}
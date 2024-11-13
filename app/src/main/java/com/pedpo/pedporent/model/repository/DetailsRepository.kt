package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailsData
import com.pedpo.pedporent.utills.ContextApp
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class DetailsRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }

    fun detailsMarkets(viewTO: ViewTO): MutableLiveData<DataWrapper<DetailsData>> {
        var mutable = MutableLiveData<DataWrapper<DetailsData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testDetail", "detailsMarkets: ${throwable.message}" )
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response =  if (viewTO.type == ContextApp.MARKET)
            serviceAPI?.detailsMarket(viewTO);
            else
              serviceAPI?.detailsSerivce(viewTO);

            Log.i("testDetail", "detailsMarkets: ${response?.isSuccessful}" )
            Log.i("testDetail", "detailsMarkets: ${response?.code()}" )
            Log.i("testDetail", "detailsMarkets: ${viewTO.type}" )


            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true ) {
                    mutable.value = DataWrapper(null, response.body())
                } else
                    mutable.value = DataWrapper(Exception(response?.message()), null)
            }
        }

        return mutable;
    }

    fun photoDetails(id: String,typeAPI :String): MutableLiveData<DataWrapper<PhotoDetailsData>> {
        val mutable = MutableLiveData<DataWrapper<PhotoDetailsData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceAPI?.detailsPhotos(id, typeAPI = typeAPI);

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful==true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null)
                }
            }
        }

        return mutable;
    }

    fun comments(marketID: String,typeAPI: String?): MutableLiveData<DataWrapper<CommentData>> {
        var mutable = MutableLiveData<DataWrapper<CommentData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = serviceAPI?.comments(marketID = marketID,typeAPI)
            withContext(Dispatchers.Main) {

                if (response?.isSuccessful!!) {
                    mutable.postValue(DataWrapper(null, response.body()))
                } else {
                    mutable.postValue(DataWrapper(Exception(response.message()), null))
                }

            }

        }

        return mutable;
    }

    fun sendComment(addComment: AddComment): MutableLiveData<DataWrapper<ResponseTO>> {

        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.sendComments(addComment = addComment)

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

    fun addBookmark(marketID: String?,type:String?): MutableLiveData<DataWrapper<BookmarkData>> {

        var mutable = MutableLiveData<DataWrapper<BookmarkData>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testbookmark", "exceptionHandler: " + throwable.localizedMessage)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.addBookmark(marketID, type = type)

            withContext(Dispatchers.Main) {

                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    Log.e("testbookmark", "else: ${response.body()?.message} ${response.code()} ")
                    mutable.value = DataWrapper(Exception(response.message()), null);
                }
            }
        }

        return mutable;
    }

    fun view(viewTO: ViewTO): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.view(viewTO = viewTO)

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
    fun like(viewTO: ViewTO): MutableLiveData<DataWrapper<LikeData>> {
        var mutable = MutableLiveData<DataWrapper<LikeData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.like(viewTO = viewTO)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response.body()?.message), null)
                }
            }

        }
        return mutable;
    }
    fun checkRenter(userID:String): MutableLiveData<DataWrapper<RenterData>> {
        val mutable = MutableLiveData<DataWrapper<RenterData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testUserID", "response ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = serviceAPI?.checkRenter(userID = userID)
            Log.e("testUserID", "response $userID")
            Log.e("testUserID", "response ${response?.message()}")
            Log.e("testUserID", "response ${response?.code()}")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null)
                }
            }

        }
        return mutable;
    }

    fun getStatusComment(marketID:String?): MutableLiveData<DataWrapper<ResponseComment>> {
        val mutable = MutableLiveData<DataWrapper<ResponseComment>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testUserID", "response ${throwable.message}")
            mutable.postValue(DataWrapper(java.lang.Exception(throwable), null))
        }


        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = serviceAPI?.getStatusComment(marketID = marketID?:"")
            Log.e("testUserID", "response $marketID")
            Log.e("testUserID", "response ${response?.message()}")
            Log.e("testUserID", "response ${response?.code()}")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null)
                }
            }

        }
        return mutable;
    }



}
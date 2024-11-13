package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.LiSeeningAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.liseening.LiseeningData
import com.pedpo.pedporent.model.liseening.RequestLiseening
import kotlinx.coroutines.*
import javax.inject.Inject

class LiseeningRepository @Inject constructor(private val liSeeningAPI: LiSeeningAPI) {


    fun createLiSeen(liseening: RequestLiseening):MutableLiveData<DataWrapper<ResponseTO>>{
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();
        val exceptionHanlder = CoroutineExceptionHandler{ _ ,throwable->
            Log.e("testFilter",  "fragment ${throwable.message} " )
            mutable?.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHanlder).launch {

            val response = liSeeningAPI?.createLiSeening(liseening = liseening)

            withContext(Dispatchers.Main){

                Log.i("testFilter",  "fragment ${response.isSuccessful} " )

                if (response?.isSuccessful){
                    mutable?.value = DataWrapper(null,response?.body())
                }else{
                    mutable?.value = DataWrapper(Exception("connection error"),null)
                }

            }

        }

        return mutable
    }
    fun getLiseening(type:String?):MutableLiveData<DataWrapper<LiseeningData>>{

        val mutable = MutableLiveData<DataWrapper<LiseeningData>>();
        val exceptionHandler = CoroutineExceptionHandler {_,throwable ->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = liSeeningAPI.getLiseens(type = type?:"")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful){
                    mutable.value = DataWrapper(null,response?.body())

                }else{
                    mutable.value = DataWrapper(Exception(response?.message()),null)
                }
            }
        }

        return mutable;
    }

    fun deleteLiseening(id:String?):MutableLiveData<DataWrapper<ResponseTO>>{

        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler{_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = liSeeningAPI.deleteLiSeening(id?:"")

            withContext(Dispatchers.Main){

                if (response.isSuccessful){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(null,response.body())
                }
            }
        }

        return mutable;
    }


}
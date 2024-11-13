package com.pedpo.pedporent.model.repository

import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.contactUs.RequestContactUs
import kotlinx.coroutines.*
import javax.inject.Inject

class ContactUsRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }

    fun contactUS(requestContactUs:RequestContactUs): MutableLiveData<DataWrapper<ResponseTO>>? {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceAPI?.contactUS(requestContactUs)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null,response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message),null)
                }
            }

        }

        return mutable;

    }

}
package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.AdMarketTO
import com.pedpo.pedporent.model.market.EditMarketGetData
import com.pedpo.pedporent.model.market.editMarket.SubmitMarketTO
import com.pedpo.pedporent.model.market.editMarket.SubmitServiceTO
import com.pedpo.pedporent.utills.ContextApp
import kotlinx.coroutines.*
import javax.inject.Inject

class AdMarketRepository @Inject constructor(private val serviceApi: ServiceAPI) {


    fun adMarket(adMarketTO: AdMarketTO): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("adMarket", "repository excehandler : " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable.message), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.adMarket(adMarketTO = adMarketTO)

            withContext(Dispatchers.Main) {
                Log.i("adMarket", "repository success : " + response?.isSuccessful)
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response.body()?.message), null)
                    Log.e("adMarket", "repository excehandler : " + response.code())
                    Log.e("adMarket", "repository excehandler : " + response.message())
                    Log.e("adMarket", "repository excehandler : " + response.body()?.message)
                }
            }

        }
        return mutable;
    }

    fun adService(adMarketTO: AdMarketTO): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("adMarket", "repository excehandler : " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable.message), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.adService(adMarketTO = adMarketTO)

            withContext(Dispatchers.Main) {
                Log.i("adMarket", "repository success : " + response?.isSuccessful)
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response.body()?.message), null)
                    Log.e("adMarket", "repository excehandler : " + response.code())
                    Log.e("adMarket", "repository excehandler : " + response.message())
                    Log.e("adMarket", "repository excehandler : " + response.body()?.message)
                }
            }

        }
        return mutable;
    }

    fun getAllCategories(id: String): MutableLiveData<DataWrapper<CategoryData>> {
        var mutable = MutableLiveData<DataWrapper<CategoryData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceApi?.getCategoriesOrSubcategory(id)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {

                    var data = response.body();
                    data?.data?.type = if (id.isEmpty())
                        ContextApp.CATEGORY;
                    else
                        ContextApp.SUB_CATEGORY;

                    mutable.value = DataWrapper(exception = null, data = data)
                } else {
                    mutable.value =
                        DataWrapper(exception = Exception(response.message()), data = null)
                }
            }
        }

        return mutable;
    }

    fun getAllCategoriesService(): MutableLiveData<DataWrapper<CategoryData>> {
        var mutable = MutableLiveData<DataWrapper<CategoryData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceApi?.categorysService()

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {

                    var data = response.body();

                    mutable.value = DataWrapper(exception = null, data = data)
                } else {
                    mutable.value =
                        DataWrapper(exception = Exception(response.message()), data = null)
                }
            }
        }

        return mutable;
    }

    fun getEditMarket(marketID: String): MutableLiveData<DataWrapper<EditMarketGetData>>? {

        var mutable = MutableLiveData<DataWrapper<EditMarketGetData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.editMarketGet(marketID = marketID)


            withContext(Dispatchers.Main) {
                Log.i(
                    "testMyItem", "getEditMarket: " +
                            "${response?.isSuccessful} \r\t " +
                            "${response?.body()?.data?.title} \r\t "
                )
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }

        return mutable;
    }

    fun getEditService(serviceID: String): MutableLiveData<DataWrapper<EditMarketGetData>>? {

        var mutable = MutableLiveData<DataWrapper<EditMarketGetData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.editServiceGet(serviceID = serviceID)


            withContext(Dispatchers.Main) {

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }

        return mutable;
    }

    fun submitEditMarket(submitMarketTO: SubmitMarketTO): MutableLiveData<DataWrapper<ResponseTO>>? {

        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("adEditMarket", "exceptionHandler: ${throwable.localizedMessage}")

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.submitEditMarket(submitMarketTO = submitMarketTO)
            Log.i("adEditMarket", "submitEditMarket: ${response?.code()}")

            withContext(Dispatchers.Main) {
                Log.i(
                    "adEditMarket",
                    "submitEditMarket: ${response?.isSuccessful} \r\t ${response?.body()?.message}"
                )
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }

        return mutable;

    }

    fun submitEditService(submitMarketTO: SubmitServiceTO): MutableLiveData<DataWrapper<ResponseTO>>? {

        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("adEditMarket", "exceptionHandler: ${throwable.localizedMessage}")

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceApi?.submitEditService(submitMarketTO = submitMarketTO)
            Log.i("adEditMarket", "submitEditMarket: ${response?.code()}")

            withContext(Dispatchers.Main) {
                Log.i(
                    "adEditMarket",
                    "submitEditMarket: ${response?.isSuccessful} \r\t ${response?.body()?.message}"
                )
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }

        return mutable;

    }



}
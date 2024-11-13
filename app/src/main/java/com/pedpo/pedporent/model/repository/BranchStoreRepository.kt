package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.StoreAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.branche.*
import com.pedpo.pedporent.model.store.branche.add.AddAddressBrancheTO
import com.pedpo.pedporent.model.store.branche.AddressBranchData
import com.pedpo.pedporent.model.store.branche.add.AddBranchData
import com.pedpo.pedporent.model.store.branche.time.EnableTimeResponse
import com.pedpo.pedporent.model.store.branche.time.EnableWorkTime
import com.pedpo.pedporent.model.store.branche.time.TimeBranchData
import com.pedpo.pedporent.model.store.branche.time.TimeBranchRequest
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject


class BranchStoreRepository @Inject constructor(private val storeAPI:StoreAPI) {


     fun addAddressBranch(addAddressBrancheTO: AddAddressBrancheTO):MutableLiveData<DataWrapper<AddBranchData>>{
        val mutable = MutableLiveData<DataWrapper<AddBranchData>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.addBrancheAddress(addAddressBrancheTO = addAddressBrancheTO)

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }

     fun editAddressBranch(addAddressBrancheTO: AddAddressBrancheTO):MutableLiveData<DataWrapper<ResponseTO>>{
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.editAddressBranch(addAddressBrancheTO = addAddressBrancheTO)

            Log.i("testAddressBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testAddressBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }

     fun getAddressBranch(brancheID:String?):MutableLiveData<DataWrapper<AddressBranchData>>{
        val mutable = MutableLiveData<DataWrapper<AddressBranchData>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.getAddressBranch(branchID = brancheID?:"")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.message()) , null)
                }
            }
        }

        return mutable;
    }

     fun branches(storeID:String?):MutableLiveData<DataWrapper<BranchesData>>{
        val mutable = MutableLiveData<DataWrapper<BranchesData>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response : Response<BranchesData> = if (storeID.isNullOrEmpty())
                storeAPI.branches();
            else
                storeAPI.getBranchesUser(storeID = storeID);

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }


    fun addTimeBranch(timeBranchRequest: TimeBranchRequest):MutableLiveData<DataWrapper<ResponseTO>>{
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.addWorkTime(branchRequestTime = timeBranchRequest)
            Log.i("testTimeBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testTimeBranche", "onSuccess: ${response?.message()}")
            Log.i("testTimeBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }


    fun deleteWorkTime(deleteTimeRequest: DeleteTimeRequest):MutableLiveData<DataWrapper<ResponseTO>>{
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable ->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.deleteWorkTime(deleteTimeRequest = deleteTimeRequest)
            Log.i("testTimeBranche", "onSuccess: ${deleteTimeRequest.workTimeID}")
            Log.i("testTimeBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testTimeBranche", "onSuccess: ${response?.message()}")
            Log.i("testTimeBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }


    fun enableWorkTime(enableWorkTime: EnableWorkTime):MutableLiveData<DataWrapper<EnableTimeResponse>>{
        val mutable = MutableLiveData<DataWrapper<EnableTimeResponse>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable ->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.enableWorkTime(enableWorkTime = enableWorkTime)
            Log.i("testTimeBranche", "onSuccess: ${enableWorkTime.workTimeID}")
            Log.i("testTimeBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testTimeBranche", "onSuccess: ${response?.message()}")
            Log.i("testTimeBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }

    fun deleteBranch(branchID: String?):MutableLiveData<DataWrapper<ResponseTO>>{
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        val exceptionHandler = CoroutineExceptionHandler {_,throwable ->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.deleteBranch(branchID = branchID?:"")

            Log.i("deleteBracnh", "onSwiped: ${response?.isSuccessful}")
            Log.i("deleteBracnh", "onSwiped: ${response?.message()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }

    fun branchDetail(brancheID: String?):MutableLiveData<DataWrapper<BranchDetailData>>{
        val mutable = MutableLiveData<DataWrapper<BranchDetailData>>();

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.detailBranch(branchID = brancheID?:"")
            Log.i("testTimeBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testTimeBranche", "onSuccess: ${response?.message()}")
            Log.i("testTimeBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }

    fun getTimeBranch(brancheID: String?):MutableLiveData<DataWrapper<TimeBranchData>>{
        val mutable = MutableLiveData<DataWrapper<TimeBranchData>>();

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = storeAPI.getTimeBranch(branchID = brancheID?:"")
            Log.i("testTimeBranche", "onSuccess: ${response?.isSuccessful}")
            Log.i("testTimeBranche", "onSuccess: ${response?.message()}")
            Log.i("testTimeBranche", "onSuccess: ${response?.code()}")

            withContext(Dispatchers.Main){
                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null , response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message) , null)
                }
            }
        }

        return mutable;
    }



}
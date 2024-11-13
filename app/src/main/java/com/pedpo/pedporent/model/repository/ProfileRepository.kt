package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ProfileAPI
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.bookmark.BookmarkData
import com.pedpo.pedporent.model.myItems.MyItemsData
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.profile.CalendarData
import com.pedpo.pedporent.model.profile.ProfileData
import com.pedpo.pedporent.model.renterMarket.RenterMarketData
import com.pedpo.pedporent.model.store.*
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.edit.ResponseEditStorePhotos
import com.pedpo.pedporent.model.store.storeList.StoreListData
import com.pedpo.pedporent.utills.ContextApp
import kotlinx.coroutines.*
import javax.inject.Inject

class ProfileRepository {

    private var profileAPI: ProfileAPI? = null;


    @Inject
    constructor(profileAPI: ProfileAPI) {
        this.profileAPI = profileAPI;
    }

    fun profile(): MutableLiveData<DataWrapper<ProfileData>>? {

        val mutable = MutableLiveData<DataWrapper<ProfileData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testProfile", "false: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.profile();

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    Log.i("testProfile", "profile: ${response.body()?.message}")
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    Log.e("testProfile", "false: ${response?.body()?.message}")
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable;
    }


    fun setFirstName(firstName: String): MutableLiveData<DataWrapper<ResponseTO>>? {
        Log.i("testProfile", "setFirstName: ")

        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("testProfile", "throwable: ${throwable.localizedMessage}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.setFirstName(firstName)

            withContext(Dispatchers.Main) {
                Log.i("testProfile", "profile: ${response?.body()?.message}")

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun setLastName(firstName: String): MutableLiveData<DataWrapper<ResponseTO>>? {

        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.setLastName(firstName)

            withContext(Dispatchers.Main) {

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response?.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun setCityID(cityID: String?): MutableLiveData<DataWrapper<ResponseTO>> {

        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.setCityProfile(cityID)

            withContext(Dispatchers.Main) {

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response?.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun setImageProfile(requestProfilePhoto: RequestProfilePhoto): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        val exceptiontHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testAvatar", "error : ${throwable.localizedMessage}")
            mutable.postValue(DataWrapper(Exception(throwable), null));
        }

        CoroutineScope(Dispatchers.IO + exceptiontHandler).launch {

            val response = profileAPI?.setImageProfile(requestProfilePhoto)

            withContext(Dispatchers.Main) {
                Log.e("testAvatar", "error : ${response?.isSuccessful}")
                Log.e("testAvatar", "error : ${response?.message()}")
                Log.e("testAvatar", "error : ${response?.code()}")

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }

        return mutable;
    }

    fun myItems(): MutableLiveData<DataWrapper<MyItemsData>>? {
        val mutable = MutableLiveData<DataWrapper<MyItemsData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null));

        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.myItems()

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

    fun bookmarks(): MutableLiveData<DataWrapper<BookmarkData>> {
        val mutable = MutableLiveData<DataWrapper<BookmarkData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("testBookmark", "bookmarks: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.bookmarks()

            withContext(Dispatchers.Main) {
                Log.i("testBookmark", "bookmarks: ${response?.isSuccessful}")

                if (response?.isSuccessful == true)
                    mutable.value = DataWrapper(null, response.body())
                else
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)

            }
        }

        return mutable;
    }

    fun deleteMarket(myMarket: MyMarkets?): MutableLiveData<DataWrapper<ResponseTO>>? {

        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = profileAPI?.deleteMarket(
                myMarket?.marketID,
                if (myMarket?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
            )


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

    fun deActiveMarket(activeMarketTO: ActiveMarketTO?): MutableLiveData<DataWrapper<ResponseTO>> {

        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testDeactive", "deActiveMarket: ${throwable.localizedMessage}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = profileAPI?.deactiveMarket(activeMarketTO);
            Log.i("testDeactive", "deActiveMarket: ${response?.isSuccessful}")
            Log.i("testDeactive", "deActiveMarket: ${response?.code()}")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    Log.i("testDeactive", "deActiveMarket: ${response?.body()?.message}")

                    mutable.value = DataWrapper<ResponseTO>(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun renterMarkets(userID: String, ip: String): MutableLiveData<DataWrapper<RenterMarketData>> {
        Log.e("testRenter", "userID : ${userID}")

        var mutable = MutableLiveData<DataWrapper<RenterMarketData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testRenter", "renterMarkets: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = profileAPI?.renterMarkets(userID = userID, ip)

            withContext(Dispatchers.Main) {
                Log.i(
                    "testRenter",
                    "renterMarkets: ${response?.isSuccessful} \r\t " +
                            "${response?.message()}",
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

    fun datesDeActive(
        marketID: String?,
        type: String?
    ): MutableLiveData<DataWrapper<CalendarData>> {
        Log.e("testCalendr", "userID : ${marketID}")

        var mutable = MutableLiveData<DataWrapper<CalendarData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testCalendr", "renterMarkets: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = profileAPI?.dateOfInActive(marketID = marketID ?: "", type = type ?: "")

            withContext(Dispatchers.Main) {
                Log.i(
                    "testCalendr",
                    "renterMarkets: ${response?.isSuccessful} \r\t " +
                            "${response?.message()}",
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

    fun active(marketID: String?, type: String?): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = profileAPI?.active(marketID = marketID ?: "", type = type ?: "")
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response?.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable;
    }

    /** Store **/

    fun createRepository(storeRequest: StoreRequest?): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryStore", "onException: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = profileAPI?.createStore(storeRequest ?: StoreRequest())

            withContext(Dispatchers.Main) {
                Log.e("categoryStore", "onException: ${response?.isSuccessful}")
                Log.e("categoryStore", "onException: ${response?.code()}")
                Log.e("categoryStore", "onException: ${response?.message()}")
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable;
    }

    fun photosStore(): MutableLiveData<DataWrapper<ResponseStorePhotos>> {

        val mutable = MutableLiveData<DataWrapper<ResponseStorePhotos>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("responseStore", ":Repository  ${throwable.message}")

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.photosStore()

            Log.i("responseStore", "photosStore: ${response?.isSuccessful}")


            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable?.value = DataWrapper(null, response?.body())
                } else {
                    mutable?.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable

    }

    fun photosStoreUser(storeID:String?): MutableLiveData<DataWrapper<ResponseStorePhotos>> {

        val mutable = MutableLiveData<DataWrapper<ResponseStorePhotos>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("responseStore", ":Repository  ${throwable.message}")

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.photosStoreUser(storeID = storeID?:"")

            Log.i("responseStore", "photosStore: ${response?.isSuccessful}")


            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable?.value = DataWrapper(null, response?.body())
                } else {
                    mutable?.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable

    }

    fun detailStore(): MutableLiveData<DataWrapper<StoreDetials>> {
        val mutalbe = MutableLiveData<DataWrapper<StoreDetials>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("getDataStore", "detailStore: ${throwable.message}" )
            mutalbe.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.detailsStore();
            Log.e("getDataStore", "detailStore: ${response?.isSuccessful}" )

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutalbe.value = DataWrapper(null, response.body())
                } else {
                    mutalbe.value = DataWrapper(Exception(response?.message()), response?.body())
                }
            }

        }
        return mutalbe
    }

    fun detailStore(storeID: String?): MutableLiveData<DataWrapper<StoreDetials>> {
        val mutalbe = MutableLiveData<DataWrapper<StoreDetials>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutalbe?.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.detailsStoreUser(storeID = storeID?:"")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutalbe?.value = DataWrapper(null, response?.body())
                } else {
                    mutalbe?.value = DataWrapper(Exception(response?.message()), response?.body())
                }
            }

        }
        return mutalbe
    }

    fun editStore(request: StoreRequestEdit): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testEditStore", "onException: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.editStore(request = request);

            withContext(Dispatchers.Main) {
                Log.i("testEditStore", "onException: ${response?.code()}")
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response?.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }
        return mutable;
    }

    fun getEditStorePhotos(): MutableLiveData<DataWrapper<ResponseEditStorePhotos>> {
        val mutable = MutableLiveData<DataWrapper<ResponseEditStorePhotos>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testEditStore", "onExceptionn : ${throwable.message}")

            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.getEditStoresPhotoes();

            withContext(Dispatchers.Main) {
                Log.i("testEditStore", "response: ${response?.isSuccessful}")
                Log.i("testEditStore", "response: ${response?.code()}")
//                Log.i("testEditStore", "response: ${response?.body()?.data?.storePhotoes}")
                Log.i("testEditStore", "logo: ${response?.body()?.data?.logo}")
                Log.i("testEditStore", "response: ${response?.body()?.data?.madrak}")
//                Log.i("testEditStore", "response: ${response?.body()?.data?.storePhotoes?.get(0)?.image}")

                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }
        return mutable;
    }

    fun setEditStore(request: StoreDetailEditRequest): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testEditStore", "onException: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.setEditStoreDetails(request = request);

            withContext(Dispatchers.Main) {
                Log.i("testEditStore", "response: ${response?.code()}")
                Log.i("testEditStore", "response: ${response?.body()?.message}")
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }
        return mutable;
    }

    fun setEditStorePhotos(request: StorePhotoslEditRequest): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testEditStore", "onException: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.setEditStorePhotos(request = request);

            withContext(Dispatchers.Main) {
                Log.i("testEditStore", "onException: ${response?.code()}")
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }

        }
        return mutable;
    }

    fun getCategoryStore(): MutableLiveData<DataWrapper<CategoryStoreData>> {
        val mutable = MutableLiveData<DataWrapper<CategoryStoreData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.getCategoryStore();

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


    fun getStore(categoryID:String,title:String?):MutableLiveData<DataWrapper<StoreListData>>{

        var mutable = MutableLiveData<DataWrapper<StoreListData>>();

        val exceptionHandler = CoroutineExceptionHandler {_,trowable->
            mutable?.postValue(DataWrapper(Exception(trowable),null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.getStores(categoryID = categoryID, title = title?:"");

            withContext(Dispatchers.Main){
                Log.i("storeList", "onException: ${response?.isSuccessful}")

                if (response?.isSuccessful == true){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value = DataWrapper(Exception(response?.body()?.message),null)
                }
            }
        }

        return mutable;
    }

    fun poster():MutableLiveData<DataWrapper<PosterData>>{

        val mutable = MutableLiveData<DataWrapper<PosterData>>();

        var exceptionHandler = CoroutineExceptionHandler {_,throwable ->
            Log.e("testPoster", "poster: ${throwable.localizedMessage}" )
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = profileAPI?.poster();
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

    fun isStore():MutableLiveData<DataWrapper<IsStore>>{

        val mutable = MutableLiveData<DataWrapper<IsStore>>();

        var exceptionHandler = CoroutineExceptionHandler {_,throwable->
            Log.e("testStore", "onException: ${throwable?.message}")
            Log.e("testStore", "onException: ${throwable?.localizedMessage}")
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = profileAPI?.isStore();
            withContext(Dispatchers.Main){

                if (response?.isSuccessful==true){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value  = DataWrapper(Exception(response?.body()?.message),null)
                }
            }
        }

        return mutable;
    }
    fun setRateStore(rateTO:RateStoreTO?):MutableLiveData<DataWrapper<ResponseTO>>{

        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler {_,throwable->
//            Log.e("testStore", "onException: ${throwable?.message}")
//            Log.e("testStore", "onException: ${throwable?.localizedMessage}")
            mutable.postValue(DataWrapper(Exception(throwable),null))
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = profileAPI?.setRateStore(rateTO?:RateStoreTO());
            withContext(Dispatchers.Main){

                if (response?.isSuccessful==true){
                    mutable.value = DataWrapper(null,response.body())
                }else{
                    mutable.value  = DataWrapper(Exception(response?.body()?.message),null)
                }
            }
        }

        return mutable;
    }

    fun closeComment(marketID:String?): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testUserID", "response ${throwable.message}")
            mutable.postValue(DataWrapper(java.lang.Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.closeComment(marketID = marketID?:"")
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

    fun getStatusComment(marketID:String?): MutableLiveData<DataWrapper<ResponseComment>> {
        val mutable = MutableLiveData<DataWrapper<ResponseComment>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("testUserID", "response ${throwable.message}")
            mutable.postValue(DataWrapper(java.lang.Exception(throwable), null))
        }


        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = profileAPI?.getStatusComment(marketID = marketID?:"")
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
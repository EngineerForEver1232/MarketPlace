package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.PlaceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.model.place.local.PlaceAllData
import com.pedpo.pedporent.room.dao.place.CityDAO
import com.pedpo.pedporent.room.dao.place.CountryDAO
import com.pedpo.pedporent.room.dao.place.ProvinceDAO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.NetConnection
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.Exception

class LocationRepository {

    private var placeAPI: PlaceAPI? = null;
    private var countryDAO: CountryDAO? = null;
    private var provinceDAO: ProvinceDAO? = null;
    private var cityDAO: CityDAO? = null;
    private var prefManagerLanguage: PrefManagerLanguage? = null;


    @Inject
    constructor(
        placeAPI: PlaceAPI,
        countryDAO: CountryDAO, provinceDAO: ProvinceDAO, cityDAO: CityDAO,
        prefManagerLanguage: PrefManagerLanguage
    ) {
        this.placeAPI = placeAPI;
        this.countryDAO = countryDAO;
        this.cityDAO = cityDAO;
        this.provinceDAO = provinceDAO;
        this.prefManagerLanguage = prefManagerLanguage
    }

    fun getCountry(mutable: MutableLiveData<DataWrapper<PlaceData>>?): MutableLiveData<DataWrapper<PlaceData>> {

        val finalMutable : MutableLiveData<DataWrapper<PlaceData>> =
            mutable ?: MutableLiveData<DataWrapper<PlaceData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable?.postValue(DataWrapper(Exception(throwable), null))
        }

        refreshData(mutable = finalMutable)

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if ((countryDAO?.selectCountry()?.size ?: 0) > 0) {
                getCountryLocal(mutable = finalMutable)
                return@launch
            }
            insertPlacesLocal()

            val response = placeAPI?.getCountreis()
            withContext(Dispatchers.Main) {
                when (response?.isSuccessful == true) {
                    true -> {
                        var placeData = response?.body()
                        placeData?.typePlace = ContextApp.COUNTRY;
                        finalMutable?.value = DataWrapper(null, placeData);

                    }
                    false -> {
                        finalMutable?.value = DataWrapper(Exception(response?.message()), null)
                    }
                }
            }
        }
        return finalMutable;
    }

    fun getProvinces(idCountry: String, title: String?): MutableLiveData<DataWrapper<PlaceData>> {
        var mutable = MutableLiveData<DataWrapper<PlaceData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if ((provinceDAO?.selectProvinceALL()?.size ?: 0) > 0) {
                getProvincesLocal(idCountry = idCountry, title = title, mutable = mutable)
                return@launch
            }

            insertPlacesLocal()

            val response = placeAPI?.getProvinces(idCountry, title)
            withContext(Dispatchers.Main) {
                when (response?.isSuccessful!!) {
                    true -> {
                        var placeData = response.body()
                        placeData?.typePlace = ContextApp.PROVINCE;
                        mutable.value = DataWrapper(null, placeData)
                    }
                    false -> {
                        mutable.value = DataWrapper(Exception(response.message()), null)
                    }
                }
            }
        }
        return mutable;
    }

    fun getCities(idProvince: String, title: String?): MutableLiveData<DataWrapper<PlaceData>> {
        var mutable = MutableLiveData<DataWrapper<PlaceData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if ((cityDAO?.selectCityAll()?.size ?: 0) > 0) {
                getCitiesLocal(idProvince = idProvince, title = title, mutable = mutable)
                return@launch
            }

            val response = placeAPI?.getCities(idProvince, title ?: "")
            withContext(Dispatchers.Main) {
                when (response?.isSuccessful!!) {
                    true -> {
                        var placeData = response.body()
                        placeData?.typePlace = ContextApp.CITY;
                        mutable.value = DataWrapper(null, placeData)
                    }
                    false -> {
                        mutable.value = DataWrapper(Exception(response.message()), null)
                    }
                }
            }
        }
        return mutable;
    }

    fun insertPlacesLocal() {
        Log.i("testPlaceLocal", "test: ")
        var mutable = MutableLiveData<DataWrapper<PlaceAllData>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("testPlaceLocal", "exception: ${throwable.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = placeAPI?.getPlaces()
            withContext(Dispatchers.Main) {

                var responseDB = countryDAO?.insert(response?.data?.countryList ?: emptyList());

                var responseInsertProvince =
                    provinceDAO?.insert(response?.data?.provinceList ?: emptyList());


                var responseInsertCity = cityDAO?.insert(response?.data?.cityList ?: emptyList());


                if (response?.isSuccess == true) {
                    mutable.value = DataWrapper(null, response?.data)
                } else
                    mutable.value = DataWrapper(Exception("disConnecting"), response?.data)
            }

        }

    }

    fun getCountryLocal(mutable: MutableLiveData<DataWrapper<PlaceData>>): MutableLiveData<DataWrapper<PlaceData>> {
        var mutable = mutable;

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = countryDAO?.selectCountry()
            withContext(Dispatchers.Main) {

                var placeData = PlaceData();
                var listPlaceTO = arrayListOf<PlaceTO>();


                for (res in response!!) {
                    var placeTO = PlaceTO();
                    placeTO.id = res.id;
                    if (prefManagerLanguage?.language == ContextApp.EN)
                        placeTO.name = res.englishName;
                    else
                        placeTO.name = res.name;

                    placeTO.persianName = res.name;
                    placeTO.englishName = res.englishName;

                    listPlaceTO.add(placeTO)
                }
                placeData.data = listPlaceTO;
                placeData.isSuccess = true;
                placeData.typePlace = ContextApp.COUNTRY;


                mutable.value = DataWrapper(null, placeData);

            }
        }
        return mutable;
    }


    fun getProvincesLocal(
        idCountry: String,
        title: String?,
        mutable: MutableLiveData<DataWrapper<PlaceData>>
    ): MutableLiveData<DataWrapper<PlaceData>> {
        var mutable = mutable;

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = provinceDAO?.selectProvince(countryID = idCountry, title = title ?: "")
//            val response = provinceDAO?.selectProvinceALL()
            withContext(Dispatchers.Main) {

                var placeData = PlaceData();
                var listPlaceTO = arrayListOf<PlaceTO>();


                for (res in response!!) {
                    var placeTO = PlaceTO();
                    placeTO.id = res.id;
                    if (prefManagerLanguage?.language == ContextApp.EN)
                        placeTO.name = res.englishName;
                    else
                        placeTO.name = res.name;

                    placeTO.persianName = res.name;
                    placeTO.englishName = res.englishName;

                    listPlaceTO.add(placeTO)
                }
                placeData.data = listPlaceTO;
                placeData.isSuccess = true;
                placeData.typePlace = ContextApp.PROVINCE;


                mutable.value = DataWrapper(null, placeData);

            }
        }
        return mutable;
    }

    fun getCitiesLocal(
        idProvince: String,
        title: String?,
        mutable: MutableLiveData<DataWrapper<PlaceData>>
    ): MutableLiveData<DataWrapper<PlaceData>> {
        var mutable = mutable;


        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = cityDAO?.selectCity(cityID = idProvince, title = title ?: "")
//            val response = provinceDAO?.selectProvinceALL()
            withContext(Dispatchers.Main) {

                var placeData = PlaceData();
                var listPlaceTO = arrayListOf<PlaceTO>();


                for (res in response!!) {
                    var placeTO = PlaceTO();
                    placeTO.id = res.id;
                    if (prefManagerLanguage?.language == ContextApp.EN)
                        placeTO.name = res.englishName;
                    else
                        placeTO.name = res.name;

                    placeTO.persianName = res.name;
                    placeTO.englishName = res.englishName;

                    listPlaceTO.add(placeTO)
                }
                placeData.data = listPlaceTO;
                placeData.isSuccess = true;
                placeData.typePlace = ContextApp.CITY;


                mutable.value = DataWrapper(null, placeData);

            }
        }
        return mutable;
    }

    fun refreshData(mutable: MutableLiveData<DataWrapper<PlaceData>>):MutableLiveData<DataWrapper<PlaceData>> {

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(
                "placeTestSql",
                "changeData exception: ${throwable.message}"
            )
        }
        if (!NetConnection.newInstance().isDisconnect(null))
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = placeAPI?.changeData();

                withContext(Dispatchers.Main) {
                    Log.i(
                        "placeTestSql",
                        "refreshData : ${response?.isSuccessful}"
                    )

                    if (response?.isSuccessful == true) {
                        Log.i(
                            "placeTestSql",
                            "isChange_Location: ${response?.body()?.data}"
                        )
                        if (response.body()?.data == true ) {

                            CoroutineScope(Dispatchers.IO).launch {
                                provinceDAO?.deleteAll();
                                countryDAO?.deleteAll();
                                cityDAO?.deleteAll();
                            }

                            getCountry( mutable = mutable)
                        } else {
                            Log.i(
                                "placeTestSql",
                                "changeData: false"
                            )

                        }
                    } else {
//                        categoryMarket(  mutable = mutable)
                    }
                }
            }
        else{

        }
        return mutable;
    }


}
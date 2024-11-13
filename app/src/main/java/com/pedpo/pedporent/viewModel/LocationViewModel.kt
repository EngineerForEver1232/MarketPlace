package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.PlaceData2
import com.pedpo.pedporent.model.repository.LocationRepository
import com.pedpo.pedporent.room.entity.place.CountryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel : ViewModel {

    private var locationRepository:LocationRepository?=null;

    @Inject
    constructor(locationRepository: LocationRepository){
        this.locationRepository = locationRepository;
    }

    fun getCountries():LiveData<DataWrapper<PlaceData>>?{
        return locationRepository?.getCountry(null);
    }

    fun getProvinces(idCountry:String,title:String?):LiveData<DataWrapper<PlaceData>>?{
        return locationRepository?.getProvinces(idCountry = idCountry, title = title);
    }

    fun getCities(idProvince:String,title:String?=""):LiveData<DataWrapper<PlaceData>>{
        return locationRepository?.getCities(idProvince = idProvince, title = title)!!;
    }

}
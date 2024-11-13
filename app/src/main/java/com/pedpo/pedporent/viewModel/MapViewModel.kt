package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.model.MapResponse
import com.pedpo.pedporent.model.model.OneMarketMap
import com.pedpo.pedporent.model.model.RequestMapTO
import com.pedpo.pedporent.model.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {



    fun markets(requestMapTO: RequestMapTO?):LiveData<DataWrapper<MapResponse>>{
        return mapRepository.market(requestMapTO = requestMapTO)
    }

    fun oneMarkets(cityID:String):LiveData<DataWrapper<OneMarketMap>>{
        return mapRepository.onMarket(cityID = cityID)
    }

}
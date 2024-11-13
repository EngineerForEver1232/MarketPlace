package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.Category
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.Market
import com.pedpo.pedporent.model.Poster
import com.pedpo.pedporent.model.market.ShowMarketTO
import com.pedpo.pedporent.model.market.VisitMarketData
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.repository.HomeRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel : ViewModel {

    private var homeRepository: HomeRepository? = null;
    private var mutablePoster: MutableLiveData<List<Poster>>? = null;
    private var mutableMarket: MutableLiveData<List<Market>>? = null;
    private var mutableVisited: MutableLiveData<List<Market>>? = null;
    private var mutableMarketRecomended: MutableLiveData<List<Market>>? = null;
    private var mutableCategory: MutableLiveData<ArrayList<Category>>? = null;

    @Inject
    constructor(homeRepository: HomeRepository) {
        this.homeRepository = homeRepository;
        if (mutablePoster == null)
            mutablePoster = homeRepository.getPoster()
    }


    fun getMarket(): LiveData<List<Market>> {
        return mutableMarket!!;
    }

    fun getReCommended(): LiveData<List<Market>> {
        return mutableMarketRecomended!!;
    }

    fun getvisited(): LiveData<List<Market>> {
        return mutableVisited!!;
    }

    fun getCategory(): LiveData<ArrayList<Category>> {
        return mutableCategory!!;
    }

    fun getMarketPedpo(): LiveData<DataWrapper<JsonObject>> {
        return homeRepository?.getMarketPedpo()!!;
    }

    fun lastMarkets(showMarketTO: ShowMarketTO):LiveData<DataWrapper<VisitMarketData>>{
        return homeRepository?.lastMarkets(showMarketTO = showMarketTO)!!
    }
    fun poster():LiveData<DataWrapper<PosterData>>?{
        return homeRepository?.poster()
    }
//    fun refreshLastMarkets(showMarketTO: ShowMarketTO):LiveData<DataWrapper<ShowMarketData>>?{
//        return homeRepository?.lastMarkets(showMarketTO = showMarketTO)
//    }
    fun recommendedMarkets(showMarketTO: ShowMarketTO):LiveData<DataWrapper<VisitMarketData>>?{
        return homeRepository?.recommendedMarkets(showMarketTO = showMarketTO)
    }
    fun mostViewMarkets(showMarketTO: ShowMarketTO):LiveData<DataWrapper<VisitMarketData>>?{
        return homeRepository?.mostViewMarkets(showMarketTO = showMarketTO)
    }

}
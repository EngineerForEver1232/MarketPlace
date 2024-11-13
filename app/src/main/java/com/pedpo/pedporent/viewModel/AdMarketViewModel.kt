package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.AdMarketTO
import com.pedpo.pedporent.model.market.EditMarketGetData
import com.pedpo.pedporent.model.market.editMarket.SubmitMarketTO
import com.pedpo.pedporent.model.market.editMarket.SubmitServiceTO
import com.pedpo.pedporent.model.repository.AdMarketRepository
import com.pedpo.pedporent.model.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdMarketViewModel : ViewModel {

    private var adMarketRepository: AdMarketRepository? = null;
    private var categoryRepository:CategoryRepository?=null;

    @Inject
    constructor(adMarketRepository: AdMarketRepository,categoryRepository: CategoryRepository) {
        this.adMarketRepository = adMarketRepository;
        this.categoryRepository = categoryRepository;
    }

    fun adMarket(adMarketTO: AdMarketTO):LiveData<DataWrapper<ResponseTO>>{
        return adMarketRepository?.adMarket(adMarketTO = adMarketTO)!!
    }
    fun adService(adMarketTO: AdMarketTO):LiveData<DataWrapper<ResponseTO>>{
        return adMarketRepository?.adService(adMarketTO = adMarketTO)!!
    }
    fun getCategories(id :String):LiveData<DataWrapper<CategoryData>>{
        return adMarketRepository?.getAllCategories(id)!!
    }
    fun getCategoriesService():LiveData<DataWrapper<CategoryData>>?{
        return adMarketRepository?.getAllCategoriesService()
    }

    fun getEditMarket(marketID:String):LiveData<DataWrapper<EditMarketGetData>>?{
        return adMarketRepository?.getEditMarket(marketID);
    }
    fun getEditService(serviceID:String):LiveData<DataWrapper<EditMarketGetData>>?{
        return adMarketRepository?.getEditService(serviceID);
    }
    fun submitEditMarket(submitMarketTO: SubmitMarketTO): LiveData<DataWrapper<ResponseTO>>? {
        return adMarketRepository?.submitEditMarket(submitMarketTO)
    }
    fun submitEditService(submitMarketTO: SubmitServiceTO): LiveData<DataWrapper<ResponseTO>>? {
        return adMarketRepository?.submitEditService(submitMarketTO)
    }

//    fun categoriesAll():LiveData<DataWrapper<String>>?{
//        return categoryRepository?.categoryMarket()
//    }

//    fun selectParent():LiveData<DataWrapper<CategoryData>>?{
//        return categoryRepository?.selectParent();
//    }
    fun selectChild(parentID:String?):LiveData<DataWrapper<CategoryData>>?{
        return categoryRepository?.selectChild(parentID = parentID);
    }




}
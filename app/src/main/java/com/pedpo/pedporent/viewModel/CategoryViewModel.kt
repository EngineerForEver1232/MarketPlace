package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel : ViewModel {

    private var categoryRepository:CategoryRepository?=null;

    @Inject
    constructor(categoryRepository: CategoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    fun selectListCategory(isAll:Boolean):LiveData<DataWrapper<CategoryData>>?{
        return categoryRepository?.selectListCategory(isAll);
    }
    fun selectChild(parentID:String?):LiveData<DataWrapper<CategoryData>>?{
        return categoryRepository?.selectChild(parentID = parentID);
    }

    fun getCategoriesService(isAll: Boolean):LiveData<DataWrapper<CategoryData>>?{
        return categoryRepository?.selectServiceParent(isAll = isAll)
    }

    fun selectServiceChild(parentID:String?):LiveData<DataWrapper<CategoryData>>?{
        return categoryRepository?.selectServiceChild(parentID = parentID)
    }

//    fun categoriesAll():LiveData<DataWrapper<String>>?{
//        return categoryRepository?.categoryMarket()
//    }

//    fun selectParent():LiveData<DataWrapper<CategoryData>>?{
//        return categoryRepository?.selectParent();
//    }
//    fun selectChild(parentID:String?):LiveData<DataWrapper<CategoryData>>?{
//        return categoryRepository?.selectChild(parentID = parentID);
//    }

}
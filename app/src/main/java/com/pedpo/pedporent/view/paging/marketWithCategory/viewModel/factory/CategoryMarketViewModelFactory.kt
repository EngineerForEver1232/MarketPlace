package com.pedpo.pedporent.view.paging.marketWithCategory.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.viewModel.VisitedMarketViewModel;
import com.pedpo.pedporent.view.paging.marketWithCategory.viewModel.CategoryMarketViewModel;

class CategoryMarketViewModelFactory(private val serviceAPI: ServiceAPI) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(VisitedMarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryMarketViewModel(serviceAPI) as T
        }
        throw IllegalArgumentException("Exception: Unknown ViewModel class")

    }
}
package com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.repository.LastMarketRepository
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LastMarketViewModel2 @Inject constructor(private val repository: LastMarketRepository) : ViewModel() {

    fun lastMarket(categoryID: String?, ip: String?, prefApp: PrefApp, typeAPI: String?): Flow<PagingData<PaginTO>>? =
        repository.getLasted(
            categoryID = categoryID,
            ip = ip,
            prefApp = prefApp,
            typeAPI = typeAPI
        )?.cachedIn(viewModelScope)

}
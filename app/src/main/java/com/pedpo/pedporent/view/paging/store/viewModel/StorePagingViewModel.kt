package com.pedpo.pedporent.view.paging.store.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.repository.LastMarketRepository
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.store.model.TransferData
import com.pedpo.pedporent.view.paging.store.repository.StorePagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StorePagingViewModel @Inject constructor(private val repository: StorePagingRepository) : ViewModel() {



    fun lastMarket(transferData: TransferData,prefApp: PrefApp): Flow<PagingData<PaginTO>>? =
        repository.getLasted(
            transferData =  transferData,
            prefApp = prefApp,
        )?.cachedIn(viewModelScope)

}
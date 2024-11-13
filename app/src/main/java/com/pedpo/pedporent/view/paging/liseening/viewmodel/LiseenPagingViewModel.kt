package com.pedpo.pedporent.view.paging.liseening.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedpo.pedporent.view.paging.liseening.model.TransferData
import com.pedpo.pedporent.view.paging.liseening.repository.LiseenPagingRepository
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LiseenPagingViewModel @Inject constructor(private val repository: LiseenPagingRepository) : ViewModel(){



    fun lastMarket(trasferData:TransferData): Flow<PagingData<PaginTO>>? =
        repository?.getLasted(
            trasferData = trasferData
        )?.cachedIn(viewModelScope)

}
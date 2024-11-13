package com.pedpo.pedporent.view.paging.liseening.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pedpo.pedporent.api.LiSeeningAPI
import com.pedpo.pedporent.view.paging.liseening.datasource.LiseengingDataSource
import com.pedpo.pedporent.view.paging.liseening.model.TransferData
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.repository.LastMarketRepository
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiseenPagingRepository @Inject constructor(private val liSeeningAPI: LiSeeningAPI) {




    fun getLasted(trasferData:TransferData): Flow<PagingData<PaginTO>>? {
        return Pager(
            config = PagingConfig(
                pageSize = LastMarketRepository.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LiseengingDataSource(
                liSeeningAPI = liSeeningAPI,
                transferData = trasferData
            ) }
        ).flow
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }

}
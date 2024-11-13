package com.pedpo.pedporent.view.paging.seeMoreLastMarket.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.dataSource.LastMarketDataSource
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LastMarketRepository @Inject constructor(private val serviceAPI: ServiceAPI) {



    fun getLasted(categoryID: String?, ip: String?, prefApp: PrefApp, typeAPI: String?): Flow<PagingData<PaginTO>>? {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LastMarketDataSource(
                serviceAPI,
                categoryID,
                ip,
                prefApp = prefApp,
                typeAPI = typeAPI
            ) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }



}
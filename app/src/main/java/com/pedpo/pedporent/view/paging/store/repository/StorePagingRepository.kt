package com.pedpo.pedporent.view.paging.store.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pedpo.pedporent.api.ProfileAPI
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.store.dataSource.StoreDataSource
import com.pedpo.pedporent.view.paging.store.model.TransferData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StorePagingRepository @Inject constructor(private val storeAPI: ProfileAPI) {


    fun getLasted( transferData: TransferData?,prefApp: PrefApp): Flow<PagingData<PaginTO>>? {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoreDataSource(
                storeAPI= storeAPI,
                transferData = transferData,
                prefApp = prefApp,
            ) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }



}
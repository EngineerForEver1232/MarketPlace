package com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel

import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.api.ServiceAPI


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LastMarketViewModel : ViewModel {

    private var serviceAPI: ServiceAPI? = null;

    private var content: String? = null;
    private var cityID: String? = null;
    private var ip: String? = null;
    private var typeAPI: String? = null;

//    val posts: Flow<PagingData<PaginTO>> = selectPosts();

    override fun onCleared() {
        super.onCleared()

    }

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }


    fun initCotent(categoryID: String?, ip: String?, cityID: String?, typeAPI: String?) {
        this.cityID = cityID;
        this.content = categoryID;
        this.ip = ip;
        this.typeAPI = typeAPI;
    }


//    fun selectPosts(): Flow<PagingData<PaginTO>> {
//        Log.i("testPaging", "111")
//
//        return Pager(
//            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
//            pagingSourceFactory = {
//                LastMarketDataSource(
//                    serviceAPI!!,
//                    content,
//                    ip,
//                    cityID = cityID,
//                    typeAPI = typeAPI
//                )
//            }
//        ).flow.cachedIn(viewModelScope)
//    }

    companion object {
        const val PAGE_SIZE = 1
    }
}
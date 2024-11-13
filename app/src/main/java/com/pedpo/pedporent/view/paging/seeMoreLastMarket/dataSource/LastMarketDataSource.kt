package com.pedpo.pedporent.view.paging.seeMoreLastMarket.dataSource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.utills.STARTING_PAGE_INDEX
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import retrofit2.HttpException
import java.io.IOException


class LastMarketDataSource(private val serviceAPI: ServiceAPI, private val categoryID:String?, private val ip:String?, private val prefApp: PrefApp?, private val typeAPI:String?)
    :PagingSource<Int, PaginTO>(){
    override fun getRefreshKey(state: PagingState<Int, PaginTO>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
//    override fun getRefreshKey(state: PagingState<Int, PaginTO>): Int? {
//        return state.anchorPosition
//    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaginTO> {

//        Log.i("testPaging", "load: categoryID $categoryID . ip : $typeAPI")

        return try {
            val position = params.key ?: STARTING_PAGE_INDEX;

            var requestPagingTO = RequestPaginTO(position.toString(), "5");
            var requestPagingData = RequestPagingData(
                requestPagingTO,
//                cityID = prefApp?.getCityID(),
                cityID = prefApp?.getCityID(),
                categoryID =   if (categoryID?.isNullOrEmpty()==true) null else categoryID,
                subCategoryID = null,
                ip=ip,
                typeAPI = typeAPI
            );

            val response = serviceAPI.seeMoreLastMarkets(requestPagingData);


            val data =response?.body()?.data;
            val prevKey = if (position == STARTING_PAGE_INDEX) null else position- 1
            val nextKey = if (data == null || data?.isEmpty()) null else position + 1

//            Log.e("testPagingLast", "adapter dataSource ${response?.isSuccessful} ${data?.isEmpty()}" )
//            Log.e("testPagingLast", "adapter dataSource $prevKey $position  $nextKey: " )


            LoadResult.Page(
                data = data?: emptyList(),
                prevKey = prevKey,
                nextKey = nextKey
            )


        } catch (exception: IOException) {
            Log.e("testPaging", "load:  ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("testPaging", "load:  ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            Log.e("testPaging", "load:  ${exception.message}")
            LoadResult.Error(exception)
        }

    }



}
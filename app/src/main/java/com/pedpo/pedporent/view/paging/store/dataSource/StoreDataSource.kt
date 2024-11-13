package com.pedpo.pedporent.view.paging.store.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedpo.pedporent.api.ProfileAPI
import com.pedpo.pedporent.utills.STARTING_PAGE_INDEX
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.store.model.StorePagingData
import com.pedpo.pedporent.view.paging.store.model.TransferData
import retrofit2.HttpException
import java.io.IOException


class StoreDataSource(private val storeAPI: ProfileAPI,private val transferData: TransferData?, private val prefApp: PrefApp?)
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

//        Log.i("testPaging", "load: categoryID ${transferData?.storeID}")

        return try {
            val position = params.key ?: STARTING_PAGE_INDEX;

            var requestPagingTO = RequestPaginTO(position.toString(), "5");
            var requestPagingData = StorePagingData(
                requestPagingTO,
                title = transferData?.title,
//                cityID = prefApp?.getCityID(),
                categoryID =   null,
                storeID = transferData?.storeID,
                ip=transferData?.ip,
                typeAPI = transferData?.typeAPI
            );

            val response = if (transferData?.storeID.isNullOrEmpty())
             storeAPI.pagingMyStore(requestPagingData);
            else
            storeAPI.pagingStore(requestPagingData);

//            Log.i("testPaging", "load: ${response?.isSuccessful}")
//            Log.i("testPaging", "load: ${response?.body()?.data?.size}")
//            Log.i("testPaging", "load: ${response?.code()}")


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
            Log.e("responsePaginStore", "load:  ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("responsePaginStore", "load:  ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            Log.e("responsePaginStore", "load:  ${exception.message}")
            LoadResult.Error(exception)
        }

    }



}
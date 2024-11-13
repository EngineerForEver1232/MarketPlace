package com.pedpo.pedporent.view.paging.marketWithCategory.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import retrofit2.HttpException
import java.io.IOException

const val POST_STARTING_PAGE_INDEX = 1

class CategoryMarketDataSource(private val serviceAPI: ServiceAPI,private val content:String?,private val ip:String?,private val cityID:String?) :PagingSource<Int, PaginTO>(){
    override fun getRefreshKey(state: PagingState<Int, PaginTO>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaginTO> {

        Log.i("testPaging", "load: $content $ip")

        return try {
            val position = params.key ?: POST_STARTING_PAGE_INDEX;

            var requestPagingTO = RequestPaginTO(position.toString(), "20");
            var requestPagingData = RequestPagingData(
                requestPagingTO,
                cityID = cityID,
                categoryID ="",
                subCategoryID = content,
                ip=ip
            );

            val response = serviceAPI.marketWithCategory(requestPagingData)?.body()?.data
            val prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position- 1
            val nextKey = if (response?.isEmpty()!!) null else position + 1

            Log.e("testBack1", "adapter dataSource $prevKey $nextKey: " )


            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    }



}
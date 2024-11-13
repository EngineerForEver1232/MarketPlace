package com.pedpo.pedporent.view.paging.liseening.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedpo.pedporent.api.LiSeeningAPI
import com.pedpo.pedporent.utills.STARTING_PAGE_INDEX
import com.pedpo.pedporent.view.paging.liseening.model.RequestPagingLiseen
import com.pedpo.pedporent.view.paging.liseening.model.TransferData
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import retrofit2.HttpException
import java.io.IOException

class LiseengingDataSource(private val liSeeningAPI: LiSeeningAPI,private val transferData: TransferData)
    :PagingSource<Int,PaginTO>(){
    override fun getRefreshKey(state: PagingState<Int, PaginTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaginTO> {

        return try {

            val position = params.key ?: STARTING_PAGE_INDEX;

            var requestPagingTO = RequestPaginTO(position.toString(), "5");
            var requestPagingData = RequestPagingLiseen(
                requestPagingTO,
                ip=transferData?.ip,
                id = transferData?.id
            );

            val response = liSeeningAPI.itemsLiSeening(requestPagingData);
//            Log.e("testPagingLiseen", "adapter dataSource ${response?.isSuccessful} ${response?.code()}" )

            val data =response?.body()?.data;
            val prevKey = if (position == STARTING_PAGE_INDEX) null else position- 1
            val nextKey = if (data == null || data?.isEmpty()) null else position + 1

//            Log.e("testPaging", "adapter dataSource $position $prevKey $nextKey: " )


            LoadResult.Page(
                data = data?: emptyList(),
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
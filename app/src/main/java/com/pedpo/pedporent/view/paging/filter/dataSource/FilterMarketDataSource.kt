package com.pedpo.pedporent.view.paging.filter.dataSource

import android.util.Log
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedpo.pedporent.api.FilterAPI
import com.pedpo.pedporent.model.filter.*
import com.pedpo.pedporent.model.filter.car.CarAdvancedFilter
import com.pedpo.pedporent.model.filter.car.RequestFilterAdvancedCar
import com.pedpo.pedporent.model.filter.home.AdvancedFilterHome
import com.pedpo.pedporent.model.filter.home.RequestFilterAdvancedHome
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import retrofit2.HttpException
import java.io.IOException

const val POST_STARTING_PAGE_INDEX = 1

class FilterMarketDataSource(
    private val serviceAPI: FilterAPI?,
    private val data: FilterTransfer?
) : PagingSource<Int, PaginTO>() {
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

        return try {
            val position = params.key
                ?: POST_STARTING_PAGE_INDEX;

            var response: List<PaginTO>;
            var prevKey :Int ?=null;
            var nextKey :Int ?=null;


            var requestPagingTO = PagingNumber(position, 20);

            Log.i("testFilterDataSource", "load: ${data?.typeAdvanced}")
            Log.i("testFilterDataSource", "load: ${data?.typePrice}")

            var inputFilter = InputFilter(
                title = data?.title ?: "",
                cityID = data?.cityID ?: null,
                categoryID = data?.categoryID ?: null,
                subCategoryID = data?.subCategoryID ?: null,
                type = data?.typePrice ?: "",
                priceFrom = data?.priceFrom ?: null,
                priceTo = data?.priceTo ?: null,
                priceAgree = data?.priceAgree ?: false,
                free = data?.free ?: false,
                registerDate = data?.registerDate ?: null,
                typeOfPrice=data?.typeOFPrice?:"",
                iP = data?.iP ?: ""
            );

            if (data?.typePrice.isNullOrEmpty() || data?.typePrice == "null"){
                var requestFilter = TitleSearchFilter(
                    title = data?.title?:"",
                    ip = data?.iP?:"",
                    Paging = requestPagingTO
                );
                response = serviceAPI?.searchByTitle(requestFilter = requestFilter)?.body()?.data?: listOf();

                prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position - 1
                nextKey = if (response?.isEmpty()) null else position + 1
            }
            else if (data?.typePrice==ContextApp.SERVICE){
                var requestFilter = RequestFilter(
                    Paging = requestPagingTO,
                    inputFilter = inputFilter
                );
                response = serviceAPI?.filterSearchService(requestFilter = requestFilter)?.body()?.data?: listOf();

                prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position - 1
                nextKey = if (response?.isEmpty()) null else position + 1

            }else
            when (data?.typeAdvanced) {
                ContextApp.HOME  -> {

                    var advancedFilterHome = AdvancedFilterHome(
                        meterOfHouseFrom = data.meterOfHouseFrom?:0,
                        meterOfHouseTo = data?.meterOfHouseTo?:0,
                        rooms = data?.rooms?:0,
                        yearOfHouseFrom = data?.yearOfHouseFrom?:0,
                        yearOfHouseTo = data?.yearOfHouseTo?:0,
                        bathrooms = data?.bathrooms?:0
                    );

                    var requestFilterAdaHome = RequestFilterAdvancedHome(
                        inputFilter = inputFilter ,
                        advancedFilterHome,
                        requestPagingTO
                    );


                    response = serviceAPI?.filterAdvancedHome(
                        requestFilterAdvancedHome = requestFilterAdaHome
                    )?.body()?.data?: listOf();

                    Log.e("testFilter", "adapter dataSource home ${data?.typeAdvanced} ${
                        serviceAPI?.filterAdvancedHome(
                            requestFilterAdvancedHome = requestFilterAdaHome
                        )?.code()
                    }: ")

                    prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position - 1
                    nextKey = if (response?.isEmpty()) null else position + 1

                }
                ContextApp.Car -> {

                    var carAdvancedFilter = CarAdvancedFilter(
                        yearOfCarFrom = data?.yearOfCarFrom?:0,
                        yearOfCarTo = data?.yearOfCarTo?:0,
                        kilometerOfCarFrom = data?.kilometerOfCarFrom?:0,
                        kilometerOfCarTo = data?.kilometerOfCarTo?:0
                    );

                    var requestCar = RequestFilterAdvancedCar(
                        inputFilter = inputFilter ,
                        carAdvancedFilter = carAdvancedFilter,
                        requestPagingTO
                    );


                    response = serviceAPI?.filterAdvancedCar(
                        requestCar = requestCar
                    )?.body()?.data?: listOf();

                    Log.e("testFilter", "car dataSource ${data?.typeAdvanced} ${
                        serviceAPI?.filterAdvancedCar(
                            requestCar = requestCar
                        )?.code()
                    }: ")

                    prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position - 1
                    nextKey = if (response?.isEmpty()) null else position + 1

                }
                else -> {
                    var requestFilter = RequestFilter(
                        Paging = requestPagingTO,
                        inputFilter = inputFilter
                    );


                    Log.e("testFilter", "adapter dataSource ${data?.typePrice} ${
                        serviceAPI?.filterSearch(requestFilter = requestFilter)?.code()
                    }: ")

                    response = serviceAPI?.filterSearch(requestFilter = requestFilter)?.body()?.data?: listOf();

                     prevKey = if (position == POST_STARTING_PAGE_INDEX) null else position - 1
                     nextKey = if (response?.isEmpty()) null else position + 1


                }
            }




            Log.i(
                "testFilter",  "\r\n title ${data?.title} " +
                        " \n cityId ${data?.cityID}" +
                        " \ncategory ${data?.categoryID} " +
                        " \ntype ${data?.typePrice ?: ""}         " +
                        "\n priceFrom ${data?.priceFrom ?: null}" +
                        "\n priceTO ${data?.priceTo ?: null}" +
                        "\n agree  ${data?.priceAgree ?: false}" +
                        "\n free ${data?.free ?: false}" +
                        "\n date  ${data?.registerDate ?: null}" +
                        "\n advanced  ${data?.typeAdvanced ?: null}" +
                        "\n ip ${data?.iP}"
            )





            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            Log.i("testFilter", "IOException : ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.i("testFilter", "HttpException : ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            Log.i("testFilter", "Exception : ${exception.localizedMessage}")
            LoadResult.Error(exception)
        }
    }


}
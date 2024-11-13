package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.bookmark.BookmarkData
import com.pedpo.pedporent.model.myItems.MyItemsData
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.profile.CalendarData
import com.pedpo.pedporent.model.profile.ProfileData
import com.pedpo.pedporent.model.renterMarket.RenterMarketData
import com.pedpo.pedporent.model.store.*
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.edit.ResponseEditStorePhotos
import com.pedpo.pedporent.model.store.storeList.StoreListData
import com.pedpo.pedporent.model.store.user.ResponseStorePhotosUser
import com.pedpo.pedporent.view.paging.store.model.StorePagingData
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface ProfileAPI {


    @GET("Profile/UserProfile")
    suspend fun profile():Response<ProfileData>?;

    @FormUrlEncoded
    @POST("Profile/EditUserProfileName")
    suspend fun setFirstName(@Field("Name")name:String?):Response<ResponseTO>;

    @FormUrlEncoded
    @POST("Profile/EditUserProfileLastName")
    suspend fun setLastName(@Field("Name")name:String?):Response<ResponseTO>;

    @FormUrlEncoded
    @POST("Profile/EditUserProfileCity")
    suspend fun setCityProfile(@Field("CityID")cityID:String?):Response<ResponseTO>;


    @POST("Profile/EditUserProfileImage")
    suspend fun setImageProfile(@Body requestProfilePhoto: RequestProfilePhoto):Response<ResponseTO>;

    @GET("Profile/UserMarkets")
    suspend fun myItems(): Response<MyItemsData>?


    @GET("Profile/BookMarkList")
    suspend fun bookmarks(): Response<BookmarkData>?;

    @FormUrlEncoded
    @POST("Profile/DeleteMarket")
    suspend fun deleteMarket(@Field("MarketID")marketID:String?,
                             @Field("Type")typeAPI:String?): Response<ResponseTO>?;

    @POST("Profile/InActiveMarket")
    suspend fun deactiveMarket(@Body activeMarketTO: ActiveMarketTO?):Response<ResponseTO>?;

    @FormUrlEncoded
    @POST("Profile/RenterMarkets")
    suspend fun renterMarkets(
        @Field("UserID") userID: String,
    @Field("IP") ip:String):Response<RenterMarketData>?;

    @FormUrlEncoded
    @POST("Profile/EditPhone_SendCode")
    suspend fun sendLoginCode(
        @Field("PhoneNumber") phoneNumber: String
    ): Response<ResponseTO>

    @FormUrlEncoded
    @POST("Profile/EditPhone_CheckLoginCode")
    suspend fun checkLoginCode(
        @Field("PhoneNumber") phoneNumber: String,
        @Field("VerifyCode") verifyCode: String,
    ): Response<VerifyCodeTO>

    @FormUrlEncoded
    @POST("Profile/DateOfInActive")
    suspend fun dateOfInActive(
        @Field("MarketID") marketID: String,
        @Field("Type") type: String,
    ): Response<CalendarData>

    @FormUrlEncoded
    @POST("Profile/ActiveMarket")
    suspend fun active( @Field("MarketID") marketID: String,
                        @Field("Type") type: String):Response<ResponseTO>;



    /**       Store         ***/

    @POST("Store/CreateStore")
    suspend fun createStore(@Body request: StoreRequest):Response<ResponseTO>

    @GET("Store/ProfileDetailsStore")
    suspend fun detailsStore():Response<StoreDetials>

    @FormUrlEncoded
    @POST("Store/DetailsStore")
    suspend fun detailsStoreUser(@Field("StoreID")storeID: String):Response<StoreDetials>

    @GET("Store/ProfileStorePhotos")
    suspend fun photosStore():Response<ResponseStorePhotos>

    @FormUrlEncoded
    @POST("Store/StorePhotos")
    suspend fun photosStoreUser(@Field("StoreID") storeID:String):Response<ResponseStorePhotos>

//    @POST("Store/SeeMoreLastMarketsStore")
//    suspend fun pagingStore(@Body storePagingData: StorePagingData?): Response<MarketPagingData?>?
    @POST("Store/SearcchStoreMarket")
    suspend fun pagingStore(@Body storePagingData: StorePagingData?): Response<MarketPagingData?>?

    @POST("Store/ProfileStoreMarkets")
    suspend fun pagingMyStore(@Body storePagingData: StorePagingData?): Response<MarketPagingData?>?

    @POST("Store/EditStore")
    suspend fun editStore(@Body request: StoreRequestEdit): Response<MarketPagingData?>?

    @POST("Store/SetEditStoreDetails")
    suspend fun setEditStoreDetails(@Body request: StoreDetailEditRequest): Response<MarketPagingData?>?

    @POST("Store/SetEditStoresPhotoes")
    suspend fun setEditStorePhotos(@Body request: StorePhotoslEditRequest): Response<ResponseTO?>?

    @GET("Store/GetCategoryStore")
    suspend fun getCategoryStore(): Response<CategoryStoreData?>?

    @GET("Store/GetEditStoresPhotoes")
    suspend fun getEditStoresPhotoes(): Response<ResponseEditStorePhotos>?

    @FormUrlEncoded
    @POST("Store/GetStoresList")
    suspend fun getStores(@Field("CategoryStoreID") categoryID:String,
                          @Field("Title") title:String): Response<StoreListData?>?

    @GET("poster/GetAllStorePosters")
    suspend fun poster(): Response<PosterData>

    @GET("Store/IsStore")
    suspend fun isStore(): Response<IsStore>

    @POST("Store/SetRateStore")
    suspend fun setRateStore(@Body model:RateStoreTO): Response<ResponseTO>

    @FormUrlEncoded
    @POST("Profile/DisableComment")
    suspend fun closeComment(@Field("MarketID") marketID:String ): Response<ResponseTO>

    @FormUrlEncoded
    @POST("Profile/GetStatusComment")
    suspend fun getStatusComment(@Field("MarketID") marketID:String ): Response<ResponseComment>


}
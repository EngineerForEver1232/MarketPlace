package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.contactUs.RequestContactUs
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailsData
import com.pedpo.pedporent.model.market.*
import com.pedpo.pedporent.model.market.editMarket.SubmitMarketTO
import com.pedpo.pedporent.model.marketOld.MarketPedpoOldList
import com.pedpo.pedporent.model.myItems.MyItemsData
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.search.RequestSearch
import com.pedpo.pedporent.model.search.SearchCategoryData
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.model.ticket.create.TicketCreateTO
import com.pedpo.pedporent.model.ticket.details.DetailsTicketData
import com.pedpo.pedporent.model.ticket.msg.CreateTicketMsg
import com.pedpo.pedporent.model.ticket.necessary.TicketNecessaryData
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsData
import com.pedpo.pedporent.model.ticket.tickets.TicketData
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PagingData
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.RequestPagingData
import com.google.gson.JsonObject
import com.pedpo.pedporent.model.changeData.ChangeDataCategory
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.editMarket.SubmitServiceTO
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface ServiceAPI {


    @POST("Api_Tool/GetAllTool")
    fun getMarkets(): Single<MarketPedpoOldList>

    @POST("Api_Tool/GetAllTool")
    suspend fun getMarketsCotourines(): Response<JsonObject>

    @POST("Api_Tool/GetAllTool")
    fun getMarketsJson(): Single<JsonObject>

//    /====================== Rent ==================



    @FormUrlEncoded
    @POST("login/SendLoginCode")
    suspend fun sendLoginCode(
        @Field("PhoneNumber") phoneNumber: String,
        @Field("AndroidToken") firebaseToken: String?,
        @Field("HashCode") hashCode: String?
    ): Response<ResponseTO>

    @FormUrlEncoded
    @POST("login/CheckLoginCode")
    suspend fun checkLoginCode(
        @Field("PhoneNumber") phoneNumber: String,
        @Field("VerifyCode") verifyCode: String,
    ): Response<VerifyCodeTO>

    @FormUrlEncoded
    @POST("login/LoginByGmail")
    suspend fun loginByGmail(
        @Field("GoogleToken") googleToken: String,
        @Field("AndroidToken") androidToken: String
    ) : Response<VerifyCodeTO>

    @FormUrlEncoded
    @POST("login/SetAndroidToken")
    suspend fun refreshTokenFireBase(
        @Field("AndroidToken") tokenFirbase: String
    ): Response<PhotoDetailsData>

    @FormUrlEncoded
    @POST("Profile/EditUserProfileEmail")
    suspend fun editGmail(
        @Field("GoogleToken") googleToken: String
    ) : Response<VerifyCodeTO>

    @FormUrlEncoded
    @POST("Profile/EditPhone_SendCode")
    suspend fun editPhoneSendCode(
        @Field("PhoneNumber") phoneNumber: String,
        @Field("HashCode") hashCode: String?
    ): Response<ResponseTO>

    @FormUrlEncoded
    @POST("Profile/EditPhone_CheckLoginCode")
    suspend fun checkCodeEditPhone(
        @Field("PhoneNumber") phoneNumber: String,
        @Field("VerifyCode") verifyCode: String,
    ): Response<VerifyCodeTO>



//    @FormUrlEncoded
//    @POST("Market/BookMark")
//    suspend fun bookMark(@Field("MarketID") MarketID: String): Response<JsonObject>

    @POST("Market/CreateMarket")
    suspend fun adMarket(@Body adMarketTO: AdMarketTO): Response<ResponseTO>

    @POST("Service/CreateService")
    suspend fun adService(@Body adMarketTO: AdMarketTO): Response<ResponseTO>

    @GET("CategoryMarket/GetAllCategories")
    suspend fun getAllCategories(): Response<CategoryData>

    @FormUrlEncoded
    @POST("CategoryMarket/GetCategoriesOrSubcategory")
    suspend fun getCategoriesOrSubcategory(@Field("CategoryMarketID") categoryMarketID: String): Response<CategoryData>


    //    @GET("ServiceCategory")
//    suspend fun categorysService():Response<CategoryData>
    @GET("ServiceCategory/GetListCategories")
    suspend fun categorysService(): Response<CategoryData>

    @GET("Changes/GetChangesCategory")
    suspend fun changeData():Response<ChangeDataCategory>

    @GET("CategoryMarket/GetAllCategoryAndSubcategory")
    suspend fun getAllCategoryAndSubcategory(): Response<CategoryData>

    @GET("CategoryMarket/GetAllCategoryAndSubcategory")
    fun getAllCategoryTest(): Call<CategoryData>

    @GET("posts")
    fun getPosts(@Query("_page") page: Int): Call<List<Post>>


    @POST("Market/MostViewMarkets")
    suspend fun mostViewMarkets(@Body mostViewMarketTO: MostViewMarketTO): Response<MostViewMarketData>

    @POST("Market/LastMarkets")
    suspend fun lastMarkets(@Body showMarketTO: ShowMarketTO): Response<VisitMarketData>

    @POST("Market/RecommendMarkets")
    suspend fun recommendedMarkets(@Body showMarketTO: ShowMarketTO): Response<VisitMarketData>

    @POST("Market/MostViewMarkets")
    suspend fun mostViewMarkets(@Body showMarketTO: ShowMarketTO): Response<VisitMarketData>

    @FormUrlEncoded
    @POST("Market/MarketPhotos")
    suspend fun detailsPhotos(
        @Field("MarketID") marketID: String,
        @Field("Type") typeAPI: String
    ): Response<PhotoDetailsData>


    @POST("Market/DetailsMarket")
    suspend fun detailsMarket(@Body viewTO: ViewTO): Response<DetailsData>

    @POST("Service/DetailsService")
    suspend fun detailsSerivce(@Body viewTO: ViewTO): Response<DetailsData>

    //https://newsapi.org/v2/everything?q=movies&apiKey=079dac74a5f94ebdb990ecf61c8854b7&pageSize=20&page=2
//    @GET("/v2/everything")
//    suspend fun markets(
//        @Query("q") q: String?,
//        @Query("apiKey") apiKey: String?,
//        @Query("page") page: Long,
//        @Query("pageSize") pageSize: Int
//    ): Response<ShowMarketData?>?


    @POST("Market/SeeMoreMostViewMarkets")
    suspend fun seeMoreMostVisitedMarkets(@Body requestPagingData: RequestPagingData?): Response<PagingData?>?;

    @POST("Market/SeeMoreRecommendMarkets")
    suspend fun seeMoreRecommendMarkets(@Body requestPagingData: RequestPagingData?): Response<MarketPagingData?>?;

    @POST("Market/SeeMoreLastMarkets")
    suspend fun seeMoreLastMarkets(@Body requestPagingData: RequestPagingData?): Response<MarketPagingData?>?;

    @POST("Market/LastMarketsBySubCategories")
    suspend fun marketWithCategory(@Body requestPagingData: RequestPagingData?): Response<MarketPagingData?>?;


//    @POST("Search/SearchByFilter")
//    suspend fun filterSearch(@Body requestFilter: RequestFilter?): Response<MarketPagingData?>?;


    @FormUrlEncoded
    @POST("Market/DetailNeighborMarket")
    suspend fun detailNeighborMarket(@Field("MarketID") marketID: Int?): Response<DetailsData>?;

    @FormUrlEncoded
    @POST("User/CommentList")
    suspend fun comments(
        @Field("ID") marketID: String,
        @Field("Type") type: String?
    ): Response<CommentData>?;

    @POST("User/CreateComment")
    suspend fun sendComments(@Body addComment: AddComment): Response<ResponseTO>?;

    @FormUrlEncoded
    @POST("User/BookMark")
    suspend fun addBookmark(
        @Field("ID") marketID: String?,
        @Field("Type") type: String?
    ): Response<BookmarkData>?;

    @POST("User/View")
    suspend fun view(@Body viewTO: ViewTO): Response<ResponseTO>?;

    @POST("User/Like")
    suspend fun like(@Body viewTO: ViewTO): Response<LikeData>?;

    @POST("Market/SearchGetCategoryList")
    suspend fun searchCategory(@Body requestSearch: RequestSearch): Response<SearchCategoryData>?

    @POST("Market/SearchMarket")
    suspend fun searchMarket(@Body requestSearchMarket: RequestSearchMarket): Response<MarketPagingData>?

    @GET("poster/GetAllPosters")
    suspend fun poster(): Response<PosterData>

    @GET("Ticket/GetAllTicketSections")
    suspend fun ticketSections(): Response<TicketSectionsData>

    @GET("Ticket/GetAllTicketNecessary")
    suspend fun ticketNecessary(): Response<TicketNecessaryData>

    @POST("Ticket/CreateTicket")
    suspend fun ticketCreate(@Body ticketCreateTO: TicketCreateTO): Response<ResponseTO>

    @POST("Ticket/CreateTicketMsg")
    suspend fun ticketSendMsg(@Body createTicketMsg: CreateTicketMsg): Response<ResponseTO>

    @GET("Ticket/TicketList")
    suspend fun tickets(): Response<TicketData>

    @FormUrlEncoded
    @POST("Ticket/DetailsTickets")
    suspend fun detialsTicket(@Field("TicketID") ticketID: String): Response<DetailsTicketData>?

    @POST("ContactUs/ContactUs")
    suspend fun contactUS(@Body requestContactUs: RequestContactUs): Response<RenterData>

    @FormUrlEncoded
    @POST("Market/EditMarketGet")
    suspend fun editMarketGet(@Field("MarketID") marketID: String): Response<EditMarketGetData>?

    @FormUrlEncoded
    @POST("Service/EditServiceGet")
    suspend fun editServiceGet(@Field("ServiceID") serviceID: String): Response<EditMarketGetData>?

    @POST("Market/SubmitEditMarket")
    suspend fun submitEditMarket(@Body submitMarketTO: SubmitMarketTO): Response<ResponseTO>?

    @POST("Service/SubmitEditService")
    suspend fun submitEditService(@Body submitMarketTO: SubmitServiceTO): Response<ResponseTO>?

    @GET("Profile/UserMarkets")
    suspend fun myItems(): Response<MyItemsData>?

    @GET("CategoryMarket/GetListCategories")
    suspend fun categorys(): Response<CategoryData>

    @FormUrlEncoded
    @POST("Profile/RenterPhoneNumber")
    suspend fun checkRenter(@Field("UserID") userID: String): Response<RenterData>

    @FormUrlEncoded
    @POST("Profile/GetStatusComment")
    suspend fun getStatusComment(@Field("MarketID") marketID:String ): Response<ResponseComment>

}
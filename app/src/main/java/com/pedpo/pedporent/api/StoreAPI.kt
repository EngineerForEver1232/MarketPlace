package com.pedpo.pedporent.api

import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.branche.*
import com.pedpo.pedporent.model.store.branche.add.AddAddressBrancheTO
import com.pedpo.pedporent.model.store.branche.AddressBranchData
import com.pedpo.pedporent.model.store.branche.add.AddBranchData
import com.pedpo.pedporent.model.store.branche.time.EnableTimeResponse
import com.pedpo.pedporent.model.store.branche.time.EnableWorkTime
import com.pedpo.pedporent.model.store.branche.time.TimeBranchData
import com.pedpo.pedporent.model.store.branche.time.TimeBranchRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface StoreAPI {

    @GET("Store/GetListBranch")
    suspend fun branches():Response<BranchesData>


    @FormUrlEncoded
    @POST("Store/GetListBranchDetails")
    suspend fun getBranchesUser(@Field("StoreID") storeID:String):Response<BranchesData>


    @POST("Store/AddBranch")
    suspend fun addBrancheAddress(@Body addAddressBrancheTO: AddAddressBrancheTO):Response<AddBranchData>?;

    @POST("Store/SetEditBranch")
    suspend fun editAddressBranch(@Body addAddressBrancheTO: AddAddressBrancheTO):Response<ResponseTO>?;

    @FormUrlEncoded
    @POST("Store/GetEditBranch")
    suspend fun getAddressBranch(@Field("BranchID") branchID:String):Response<AddressBranchData>?;

    @FormUrlEncoded
    @POST("Store/GetEditWorkTime")
    suspend fun getTimeBranch(@Field("BranchID") branchID:String):Response<TimeBranchData>?;

    @POST("Store/AddWorkTime")
    suspend fun addWorkTime(@Body branchRequestTime: TimeBranchRequest):Response<ResponseTO>?;

    @POST("Store/DeleteWorkTime")
    suspend fun deleteWorkTime(@Body deleteTimeRequest:DeleteTimeRequest ):Response<ResponseTO>?;

    @FormUrlEncoded
    @POST("Store/DeleteBranch")
    suspend fun deleteBranch(@Field("BranchID") branchID:String):Response<ResponseTO>?;

    @POST("Store/EnableWorkTime")
    suspend fun enableWorkTime(@Body enableWorkTime: EnableWorkTime):Response<EnableTimeResponse>?;

    @FormUrlEncoded
    @POST("Store/GetBranchDetails")
    suspend fun detailBranch(@Field("BranchID") branchID:String ):Response<BranchDetailData>?;


}
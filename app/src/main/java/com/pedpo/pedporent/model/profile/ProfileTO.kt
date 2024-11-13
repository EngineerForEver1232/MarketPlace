package com.pedpo.pedporent.model.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.R
import com.pedpo.pedporent.di.utill.ApplicationPedpo

data class ProfileTO (

    var firstName:String?= null,
    var lastName:String?=null,
    var image:String?=null,
    var phoneNumber:String?=null,
    var cityName:String ?= null,
    @SerializedName("email")
    var email:String ?= null


)
package com.pedpo.pedporent.utills

import androidx.lifecycle.MutableLiveData

class MyMutable {

    companion object{
        var mutableBookmark = MutableLiveData<BooleanBookmark>()
//        var mutableHome = MutableLiveData<BooleanBookmark>()
        var mutableMarketID = MutableLiveData<String>()
        var mutableCity = MutableLiveData<String>()
        var mutable: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    }

    data class BooleanBookmark(
        var bookmark :Boolean?=false,
        var home :Boolean ?= false
    )

}
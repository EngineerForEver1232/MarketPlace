package com.pedpo.pedporent.model

class DataWrapper<T> {

    private var exception: Exception? =null;

    private var data:T?=null;

    constructor(exception: Exception?,data: T?){
        this.data =data;
        this.exception = exception;
    }

    fun getData():T{
        return  data!!;
    }

    fun hasException():Boolean{
        return exception !=null;
    }

    fun getException():Exception{
        return exception!!;
    }


}
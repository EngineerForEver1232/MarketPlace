package com.pedpo.pedporent.utills

import androidx.lifecycle.Observer
import com.pedpo.pedporent.model.DataWrapper

open class CustomObserver<T> : Observer<DataWrapper<T>> {

    interface ResultListener<T> {
        fun onSuccess(dataInput: T)
        fun onException(exception: Exception)
    }

    private var resultListener: ResultListener<T>? = null;

    constructor(resultListener: ResultListener<T>) {
        this.resultListener = resultListener;
    }

    override fun onChanged(dataWrapper: DataWrapper<T>?) {
        if (dataWrapper!=null){
            if (dataWrapper.hasException()){
                resultListener?.onException(dataWrapper.getException())
            }else {
                resultListener?.onSuccess(dataWrapper.getData())
            }
        }
    }
}
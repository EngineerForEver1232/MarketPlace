package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.liseening.LiseeningData
import com.pedpo.pedporent.model.liseening.RequestLiseening
import com.pedpo.pedporent.model.repository.LiseeningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiseeningViewModel @Inject constructor( private val liseeningRepository: LiseeningRepository) : ViewModel() {

    fun createLiseening(liseening: RequestLiseening):LiveData<DataWrapper<ResponseTO>>?{
       return liseeningRepository?.createLiSeen(liseening)
    }

    fun getLiseenings(type:String?):LiveData<DataWrapper<LiseeningData>>?{
        return liseeningRepository?.getLiseening(type = type)
    }

    fun deleteLiseening(id:String?):LiveData<DataWrapper<ResponseTO>>?{
        return liseeningRepository.deleteLiseening(id)
    }
}
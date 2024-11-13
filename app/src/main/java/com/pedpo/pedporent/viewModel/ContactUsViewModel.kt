package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.contactUs.RequestContactUs
import com.pedpo.pedporent.model.repository.ContactUsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel :ViewModel {

    private var contactUsRepository:ContactUsRepository?=null;

    @Inject
    constructor(contactUsRepository: ContactUsRepository){
        this.contactUsRepository = contactUsRepository;
    }

    fun contactUs(requestContactUs: RequestContactUs):LiveData<DataWrapper<ResponseTO>>?{
        return contactUsRepository?.contactUS(requestContactUs)
    }


}
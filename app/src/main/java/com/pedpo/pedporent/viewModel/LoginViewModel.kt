package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.repository.LoginRepository
import com.pedpo.pedporent.model.transfer.TransVerifyCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel : ViewModel {

    private var loginRepository: LoginRepository? = null;

    @Inject
    constructor(loginRepository: LoginRepository) {
        this.loginRepository = loginRepository;
    }

    fun sendSmsCode(phoneNumber: String,firebaseToken:String,hashCode:String): LiveData<DataWrapper<ResponseTO>>? {
        return loginRepository?.login(phoneNumber = phoneNumber,firebaseToken = firebaseToken, hashCode = hashCode);
    }

    fun verifySmsCode(transVerifyCode: TransVerifyCode):LiveData<DataWrapper<VerifyCodeTO>>{
        return loginRepository?.checkPhoneNumber(transVerifyCode)!!
    }
    fun editPhone(phoneNumber: String,hashCode: String): LiveData<DataWrapper<ResponseTO>> ?{
        return loginRepository?.editPhoneNumber(phoneNumber = phoneNumber, hashCode = hashCode);
    }

    fun verifySmsEditPhone(transVerifyCode: TransVerifyCode):LiveData<DataWrapper<VerifyCodeTO>>{
        return loginRepository?.checkEditPhoneNumber(transVerifyCode)!!
    }

    fun loginByGmail(googleToken: String?,firebaseToken:String):LiveData<DataWrapper<VerifyCodeTO>>?{
        return loginRepository?.loginByGmail(googleToken = googleToken, firebaseToken = firebaseToken)
    }

    fun editGmail(googleToken: String?):LiveData<DataWrapper<VerifyCodeTO>>?{
        return loginRepository?.editGmail(googleToken = googleToken)
    }
    fun refreshTokenFirbase(tokenFirbase: String?):LiveData<DataWrapper<ResponseTO>>?{
        return loginRepository?.refreshToken(tokenFirbase = tokenFirbase)
    }



}
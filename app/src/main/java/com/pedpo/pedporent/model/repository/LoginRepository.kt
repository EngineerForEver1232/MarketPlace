package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.transfer.TransVerifyCode
import kotlinx.coroutines.*
import javax.inject.Inject

class LoginRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }

    fun login(phoneNumber: String,firebaseToken:String?,hashCode:String?): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper<ResponseTO>(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.sendLoginCode(phoneNumber = phoneNumber,firebaseToken = firebaseToken,
            hashCode = hashCode);
//            Log.i("checkLogin", "login: ${response?.message()}")
//            Log.i("checkLogin", "login: ${response?.code()}")

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper<ResponseTO>(null, response.body())
                } else {
                    mutable.value = DataWrapper<ResponseTO>(Exception(response.message()), null)
                }
            }
        }

        return mutable;
    }

    fun checkPhoneNumber(transfer: TransVerifyCode): MutableLiveData<DataWrapper<VerifyCodeTO>> {
        val mutable = MutableLiveData<DataWrapper<VerifyCodeTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.checkLoginCode(
                phoneNumber = transfer.phoneNumber,
                verifyCode = transfer.verifyCode
            );

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response.message()), null);
                }
            }

        }

        return mutable;

    }

    fun loginByGmail(googleToken:String?,firebaseToken:String?): MutableLiveData<DataWrapper<VerifyCodeTO>> {
        val mutable = MutableLiveData<DataWrapper<VerifyCodeTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("loginSms", "cityID: " + throwable.localizedMessage)

            mutable.postValue(DataWrapper(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.loginByGmail(
                googleToken = googleToken?:"",
                androidToken = firebaseToken?:""
            );

            withContext(Dispatchers.Main) {
                Log.i("loginSms", "message: " + response?.message())
                Log.i("loginSms", "message: $googleToken")
                Log.i("loginSms", "message: $firebaseToken")
                Log.i("loginSms", "successful : " + response?.isSuccessful)
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null);
                }
            }

        }

        return mutable;

    }

    fun editGmail(googleToken:String?): MutableLiveData<DataWrapper<VerifyCodeTO>> {
        val mutable = MutableLiveData<DataWrapper<VerifyCodeTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.editGmail(
                googleToken = googleToken?:""
            );

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null);
                }
            }

        }

        return mutable;

    }


    fun editPhoneNumber(phoneNumber: String,hashCode: String?): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("loginSms", "onException: " + throwable.message)
            mutable.postValue(DataWrapper<ResponseTO>(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.editPhoneSendCode(phoneNumber = phoneNumber, hashCode = hashCode);
            Log.i("loginSms", "response edit : " + response?.isSuccessful)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful==true) {
                    mutable.value = DataWrapper<ResponseTO>(null, response.body())
                } else {
                    mutable.value = DataWrapper<ResponseTO>(Exception(response?.message()), null)
                }
            }
        }

        return mutable;
    }

    fun checkEditPhoneNumber(transfer: TransVerifyCode): MutableLiveData<DataWrapper<VerifyCodeTO>> {
        val mutable = MutableLiveData<DataWrapper<VerifyCodeTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("loginSms", "exceptionHandler Edit: " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.checkCodeEditPhone(
                phoneNumber = transfer.phoneNumber,
                verifyCode = transfer.verifyCode
            );
            Log.i("loginSms", "response verfiyCode Edit: " + response?.isSuccessful);
            Log.i("loginSms", "response verfiyCode Edit: " + response?.body()?.message);

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body());
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null);
                    Log.i("loginSms", "response code: " + response?.code())

                }
            }
        }
        return mutable;
    }

    fun refreshToken(tokenFirbase:String?): MutableLiveData<DataWrapper<ResponseTO>> {
        val mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("loginSms", "exceptionHandler Edit: " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable.localizedMessage), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            var response = serviceAPI?.refreshTokenFireBase(
                tokenFirbase = tokenFirbase?:""
            );
            Log.i("loginSms", "response verfiyCode Edit: " + response?.isSuccessful);
            Log.i("loginSms", "response verfiyCode Edit: " + response?.body()?.message);

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body());
                } else {
                    mutable.value = DataWrapper(Exception(response?.message()), null);
                    Log.i("loginSms", "response code: " + response?.code())

                }
            }
        }
        return mutable;
    }


}
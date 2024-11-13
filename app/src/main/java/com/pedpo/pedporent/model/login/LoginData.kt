package com.pedpo.pedporent.model.login

import com.pedpo.pedporent.model.ResponseTO
import com.google.gson.annotations.SerializedName

class LoginData : ResponseTO() {

    @SerializedName("data")
    var tokenUser: TokenUser? = null;

}
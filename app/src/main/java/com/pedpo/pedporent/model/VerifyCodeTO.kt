package com.pedpo.pedporent.model

import com.pedpo.pedporent.model.login.TokenUser
import com.google.gson.annotations.SerializedName

class VerifyCodeTO : ResponseTO() {

    @SerializedName("data")
    var tokenUser: TokenUser? = null;

}
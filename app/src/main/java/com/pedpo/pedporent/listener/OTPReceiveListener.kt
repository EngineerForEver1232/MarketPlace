package com.pedpo.pedporent.listener

interface OTPReceiveListener {

    fun onOTPReceived(otp: String?)

    fun onOTPTimeOut()

}
package com.ants.driverpartner.everywhere.activity.otp

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPassResponse

interface OtpView:BaseMainView {

    fun onSuccess(action: ForgotPassResponse?)
    fun getEmail(): String
    fun getUserid(): String
    fun getOtp() :String
    fun onVerifyOtp(responseData: VerifyOtpResponse)
}
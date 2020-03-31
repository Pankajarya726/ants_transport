package com.ants.driverpartner.everywhere.activity.resetPassword

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.otp.VerifyOtpResponse

interface ResetPasswordView:BaseMainView {



    fun getNewPassword():String
    fun getCurrentPassword():String
    fun getEmail():String
    fun onSuccess(action: VerifyOtpResponse)
}
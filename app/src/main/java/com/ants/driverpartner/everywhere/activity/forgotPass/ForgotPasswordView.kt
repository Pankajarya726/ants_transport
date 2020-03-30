package com.ants.driverpartner.everywhere.activity.forgotPass

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface ForgotPasswordView:BaseMainView {
    fun onSuccess(action: ForgotPassResponse)


    fun getEmail():String
}
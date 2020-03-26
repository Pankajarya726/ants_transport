package com.ants.driverpartner.everywhere.activity.resetPassword

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface ResetPasswordView:BaseMainView {



    fun getNewPassword():String
    fun getCurrentPassword():String
}
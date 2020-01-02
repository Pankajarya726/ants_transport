package com.tekzee.amiggos.ui.login

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.google.gson.JsonObject

class LoginPresenter {
    interface LoginMainPresenter {
        fun login(input: JsonObject)
        fun onStop()

    }

    interface LoginMainView : BaseMainView {

        fun onLoginSuccess(responseData: LoginResponse)
    }
}
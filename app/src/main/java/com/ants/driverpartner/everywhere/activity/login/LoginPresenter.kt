package com.tekzee.amiggos.ui.login

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.google.gson.JsonObject

class LoginPresenter {
    interface LoginMainPresenter{
        fun doLoginApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }

    interface LoginMainView: BaseMainView {
        //fun onLoginSuccess(responseData: LoginResponse)
    }
}
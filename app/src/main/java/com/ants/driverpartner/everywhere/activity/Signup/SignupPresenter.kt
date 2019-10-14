package com.ants.driverpartner.everywhere.activity.Signup

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.google.gson.JsonObject

class SignupPresenter {


    interface SignupMainPresenter{
        fun signupApi(
            input: JsonObject

        )
        fun onStop()
    }

    interface SignupMainView: BaseMainView {
        fun onRegisterSuccess(responseData: RegisterResponse)
    }

}
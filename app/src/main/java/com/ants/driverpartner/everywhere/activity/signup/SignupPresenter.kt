package com.ants.driverpartner.everywhere.activity.signup

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.google.gson.JsonObject
import java.io.File

class SignupPresenter {


    interface SignupMainPresenter{
        fun signupApi(
            input: JsonObject
        )

        fun uploadProfileImage(
            inputFile : File
        )
        fun onStop()
    }

    interface SignupMainView: BaseMainView {
        fun onRegisterSuccess(responseData: RegisterResponse)
        fun getContext(): Context
        fun onImageUploadSuccess(responseData: UploadImageResponse)
    }

}
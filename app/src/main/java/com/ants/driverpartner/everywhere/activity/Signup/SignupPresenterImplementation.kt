package com.tekzee.amiggos.ui.login

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Signup.RegisterResponse
import com.ants.driverpartner.everywhere.activity.Signup.SignupPresenter
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignupPresenterImplementation(
    private var mainView: SignupPresenter.SignupMainView,
    context: Context
) : SignupPresenter.SignupMainPresenter {

    var context: Context? = context
    private var disposable: Disposable? = null

    override fun signupApi(
        input: JsonObject

    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.signup(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: RegisterResponse? = response.body()

                            if (responseData != null) {
                                Log.e("adgasdghqwdg", responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                                mainView.onRegisterSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
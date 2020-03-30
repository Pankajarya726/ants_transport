package com.ants.driverpartner.everywhere.activity.otp

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPassResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OtpPresenter(private var view: OtpView, private var context: Context){



    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun verifyOptApi() {

        if (view.checkInternet()) {
            view.showProgressbar()


            var input = JsonObject()
            input.addProperty(
                "email",
                view.getEmail()
            )
            input.addProperty(
                "userid",
                view.getUserid()
            )
            input.addProperty(
                "otp",
                view.getOtp()
            )


            var headers = HashMap<String, String?>()


            headers["Content-Type"] = "application/json"

            disposable = ApiClient.instance.callverifyOptApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ForgotPassResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ForgotPassResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onSuccess(responseData)
                                    }
                                    2 -> {
                                        view.validateError(responseData.message)
                                    }
                                }
                            } else {
                                view.validateError("Something went wrong!")
                            }

                        }
                    }
                }, { error ->
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }
    }


    fun callForgotPasswordApi() {

        if (view.checkInternet()) {
            view.showProgressbar()


            var input = JsonObject()
            input.addProperty(
                "email",
                view.getEmail()
            )


            var headers = HashMap<String, String?>()


            headers["Content-Type"] = "application/json"

            disposable = ApiClient.instance.callForgotPasswordApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ForgotPassResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ForgotPassResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onSuccess(responseData)
                                    }
                                    2 -> {
                                        view.validateError(responseData.message)
                                    }
                                }
                            } else {
                                view.validateError("Something went wrong!")
                            }

                        }
                    }
                }, { error ->
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }
    }
}
package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.activity.otp.VerifyOtpResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class ResetPasswordPresenter(private var view: ResetPasswordView, context: Context) {


    var context: Context? = context
    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun callResetPasswrodApi() {

        if (view.checkInternet()) {
            view.showProgressbar()


            var input = JsonObject()
            input.addProperty(
                "email",
                view.getEmail()
            )

            input.addProperty(
                "new_password",
                view.getNewPassword()
            )


            var headers = HashMap<String, String?>()

            headers["Content-Type"] = "application/json"

            disposable = ApiClient.instance.callResetPasswrodApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<VerifyOtpResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: VerifyOtpResponse? = response.body()
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

    fun callChangePasswrodApi() {

        if (view.checkInternet()) {
            view.showProgressbar()


            val userId = Utility.getSharedPreferences(context!!, Constant.USER_ID)!!.toRequestBody(MultipartBody.FORM)
            val old_password =view.getCurrentPassword().toRequestBody(MultipartBody.FORM)
            val new_password = view.getNewPassword().toRequestBody(MultipartBody.FORM)


            var headers = HashMap<String, String?>()

//            headers["Content-Type"] = "application/json"
            headers["api-key"] = Utility.getSharedPreferences(context!!,Constant.API_KEY)

            disposable = ApiClient.instance.callChangePasswrodApi(headers, userId,old_password,new_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<VerifyOtpResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: VerifyOtpResponse? = response.body()
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
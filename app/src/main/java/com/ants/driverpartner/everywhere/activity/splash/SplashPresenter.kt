package com.ants.driverpartner.everywhere.activity.splash

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class SplashPresenter(private var view: SplashView, private var context: Context) {


    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun validateAppVersion(versionName: String) {

        if (view.checkInternet()) {


            Utility.showProgressBar(context)


            var input = JsonObject()
            input.addProperty(
                "device_token",
                Utility.getDeviceToken(
                    context!!, Constant.D_TOKEN
                ).toString()
            )
            input.addProperty(
                "device_type",
                2
            )
            input.addProperty(
                "appversion",
                versionName
            )
            input.addProperty(
                "driver_id",
                ""
            )


            var headers = HashMap<String, String?>()

            headers["Content-Type"] ="application/json"


            disposable = ApiClient.instance.validateAppVersion(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ValidateAppResponse> ->
                    Utility.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ValidateAppResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.OnUpdate(responseData)
                                    }
                                    1 -> {
                                        view.onNoUpdate()
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
                    Utility.hideProgressbar()
                    view.validateError(error.message.toString())
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }
    }
}
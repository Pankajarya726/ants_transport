package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ResetPasswordPresenter(private var view: ResetPasswordView,context: Context){



    var context: Context? = context
    private var disposable: Disposable? =null
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
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(context!!, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)

            disposable = ApiClient.instance.callGetDriverListApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetDriverListResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetDriverListResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
//                                        view.onGetDriverList(responseData)
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
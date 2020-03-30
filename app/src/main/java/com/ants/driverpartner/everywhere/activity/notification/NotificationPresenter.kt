package com.ants.driverpartner.everywhere.activity.notification

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.utils.Utility
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class NotificationPresenter(private var view: NotificationView, private var context: Context) {


    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    fun getNotification() {
        view.showProgressbar()
        if (view.checkInternet()) {


            Log.e("Internet Connection", view.checkInternet().toString())


            val pageno = RequestBody.create(MultipartBody.FORM, "1")

            val userId = Utility.getSharedPreferences(context ,  Constant.USER_ID).let {
                it.toString()
                    .toRequestBody(MultipartBody.FORM)
            }


            var headers = HashMap<String, String?>()


            headers["Content-Type"] = "application/json"

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)

            disposable = ApiClient.instance.getNotification(headers, pageno,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<NotificationResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    Log.e(javaClass.simpleName, responseCode.toString())
                    Log.e(javaClass.simpleName, response.toString())
                    when (responseCode) {
                        200 -> {
                            val responseData: NotificationResponse? = response.body()

                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData!!.status == 1) {
                                view.onGetNotification(responseData)
                            } else if (responseData.status == 0) {
                                Log.e(
                                    javaClass.simpleName + "\tApi output\n\n",
                                    responseData.status.toString()
                                )
                                view.validateError(responseData.message)
                            } else {

                            }
                        }
                    }
                }, { error ->
                    Log.e(
                        javaClass.simpleName + "\tApi output\n\n",
                        "jhagdfhjgsd"
                    )
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })
        } else {
            view.hideProgressbar()
            view.validateError(context.getString(R.string.check_internet))
        }
    }

    fun deleteNotification(notification_id: Int) {
        view.showProgressbar()
        if (view.checkInternet()) {


            Log.e("Internet Connection", view.checkInternet().toString())


            val notification_id = notification_id.toString().toRequestBody(MultipartBody.FORM)

            val userId = Utility.getSharedPreferences(context ,  Constant.USER_ID).let {
                it.toString()
                    .toRequestBody(MultipartBody.FORM)
            }


            var headers = HashMap<String, String?>()


            headers["Content-Type"] = "application/json"

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)

            disposable = ApiClient.instance.deleteNotification(headers, notification_id,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<NotificationResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    Log.e(javaClass.simpleName, responseCode.toString())
                    Log.e(javaClass.simpleName, response.toString())
                    when (responseCode) {
                        200 -> {
                            val responseData: NotificationResponse? = response.body()

                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData!!.status == 1) {
                                view.onRemoveNotification(responseData)
                            } else if (responseData.status == 0) {
                                Log.e(
                                    javaClass.simpleName + "\tApi output\n\n",
                                    responseData.status.toString()
                                )
                                view.validateError(responseData.message)
                            } else {

                            }
                        }
                    }
                }, { error ->
                    Log.e(
                        javaClass.simpleName + "\tApi output\n\n",
                        "jhagdfhjgsd"
                    )
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })
        } else {
            view.hideProgressbar()
            view.validateError(context.getString(R.string.check_internet))
        }
    }
}
package com.ants.driverpartner.everywhere.fragment.scheduleBooking

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class SchedulePresenter(private var view: ScheduleView,private var context: Context) {



    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun getScheculeBooking() {

        if (view.checkInternet()) {


            Utility.showProgressBar(context)


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)

//            headers["api-key"] = "LxGS0HxbaqJPCqepHs1a4NItdCiC6uD68TmjKD4HpsIhVyqrKTF7CAh0WjJm"
//            headers["userid"] = "1"

            disposable = ApiClient.instance.callGetScheduleBookingApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ScheduleBookingResponse> ->
                    Utility.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ScheduleBookingResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onGetScheduleBooking(responseData)
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
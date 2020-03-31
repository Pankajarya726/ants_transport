package com.ants.driverpartner.everywhere.fragment.newBooking

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.fragment.newBooking.model.BookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class NewBookingPresenter(private var view: NewBookingView, private var context: Context) {


    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun getCurrentBooking() {

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


            disposable = ApiClient.instance.callGetNewBookingApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetNewBookingResponse> ->
                    Utility.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetNewBookingResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onGetNewBooking(responseData)
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

    fun callAcceptBookingApi(bookingId: Int) {
        if (view.checkInternet()) {


            Utility.showProgressBar(context)


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )

            input.addProperty("booking_id", bookingId)
            input.addProperty("status", 1)


            var headers = HashMap<String, String?>()
            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)
            disposable = ApiClient.instance.callAcceptBookingApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BookingResponse> ->
                    Utility.hideProgressbar()
                    val responseCode = response.code()
                    Log.e(javaClass.simpleName, response.body().toString())
                    when (responseCode) {
                        200 -> {
                            val responseData: BookingResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onAcceptBooking(responseData)
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
                    view.validateError(error.toString())
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }

    }

    fun callDeclineBookingApi(bookingId: Int, position: Int) {
        if (view.checkInternet()) {


            Utility.showProgressBar(context)


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )

            input.addProperty("booking_id", bookingId)
            input.addProperty("status", 2)


            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)

            disposable = ApiClient.instance.callAcceptBookingApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BookingResponse> ->
                    Utility.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: BookingResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onDeclineBooking(responseData, position)
                                    }
                                    2 -> {
                                        view.validateError(responseData.message)
                                    }
                                }
                            } else {
                                view.validateError("Something went wrong! Please try after some time")
                            }

                        }
                    }

                }, { error ->
                    Utility.hideProgressbar()
                    view.validateError("Something went wrong! Please try after some time")
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }

    }


}
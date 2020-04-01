package com.ants.driverpartner.everywhere.fragment.currentBooking

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class CurrentPresenter(private var view:Currentview,private var context: Context) {


    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun getCurrentBooking() {

        if (view.checkInternet()) {


//            Utility.showProgressBar(context)
//            Utility.showDialog(context)

            view.showProgressbar()

            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)


            disposable = ApiClient.instance.callGetCurrentBookingApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetCurrentBookingRespone> ->
//                    Utility.hideProgressbar()
//                    Utility.hideDialog()

                    view.hideProgressbar()

                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetCurrentBookingRespone? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onGetCurrentBooking(responseData)
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
//                    Utility.hideProgressbar()
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }
    }

    fun changeBookingStatus(bookingId: Int) {


        if (view.checkInternet()) {


//            Utility.showDialog(context
            view.showProgressbar()


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )
            input.addProperty(
                "booking_id",
                bookingId
            )


            input.addProperty(
                "booking_status",
                3
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context!!, Constant.USER_ID)

//            headers["api-key"] = "LxGS0HxbaqJPCqepHs1a4NItdCiC6uD68TmjKD4HpsIhVyqrKTF7CAh0WjJm"
//            headers["userid"] = "1"

            disposable = ApiClient.instance.callChangeBookingStatusApi(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ChangeBookingStatusResponse> ->
//                    Utility.hideDialog()
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ChangeBookingStatusResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onStatusChange(responseData)
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
//                    Utility.hideDialog()
                    view.hideProgressbar()
                    view.validateError(context.getString(R.string.error_message))
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }

    }


}
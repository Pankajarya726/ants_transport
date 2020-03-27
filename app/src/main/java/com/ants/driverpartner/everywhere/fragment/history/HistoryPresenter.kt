package com.ants.driverpartner.everywhere.fragment.history

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HistoryPresenter(private var view: HistoryView, private var context: Context) {

    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun getBookingHistory() {

        if (view.checkInternet()) {
            Utility.showProgressBar(context)


            var input = JsonObject()

            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context, Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()

            headers["api_key"] = Utility.getSharedPreferences(context, Constant.API_KEY)
            headers["userid"] = Utility.getSharedPreferences(context, Constant.USER_ID)


            disposable = ApiClient.instance.getHistoryBooking(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetHistroyBookingResponse> ->
                    Utility.hideProgressbar()

                    val responseCode = response.code()
                    Log.e(javaClass.simpleName, responseCode.toString())
                    Log.e(javaClass.simpleName, response.toString())
                    when (responseCode) {
                        200 -> {
                            val responseData: GetHistroyBookingResponse? = response.body()

                            Log.e(javaClass.simpleName, response.body().toString())


                            when (responseData!!.status) {

                                0 -> {
                                    view.validateError(responseData.message)
                                }

                                1 -> {
                                    view.onGetBookingHistory(responseData.data)
                                }

                                2 -> {
                                    view.validateError(responseData.message)
                                }
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
            view.validateError(context!!.getString(R.string.check_internet))
        }
    }


}
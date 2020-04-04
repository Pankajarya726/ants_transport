package com.ants.driverpartner.everywhere.activity.webView

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
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class WebViewPresenter(private var view: WebView, private var context: Context) {


    private var disposable: Disposable? = null


    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    fun loadPage(pageId: Int) {

        view.showProgressbar()
        if (view.checkInternet()) {

            Log.e("Internet Connection", view.checkInternet().toString())
            val type = "1".toRequestBody(MultipartBody.FORM)
            val userId = Utility.getSharedPreferences(context, Constant.USER_ID).toString()
                .toRequestBody(MultipartBody.FORM)


            val pageId = pageId.toString().toRequestBody(MultipartBody.FORM)


            var headers = HashMap<String, String?>()


            headers["api-key"] = Utility.getSharedPreferences(context, Constant.API_KEY)



            disposable = ApiClient.instance.loadWebPages(headers, userId, type, pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetWebViewResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetWebViewResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())


                                when(responseData.status){

                                    1->{
                                        view.onPageLoad(responseData.data)
                                    }

                                    0->{
                                        view.validateError(responseData.message)
                                    }

                                    2->{
                                        view.validateError(responseData.message)
                                    }

                                }





                            }

//                            if (responseData!!.status == 1) {
//                                view.onPageLoad(responseData.data)
//                            } else {
//                                Log.e(
//                                    javaClass.simpleName + "\tApi output\n\n",
//                                    responseData.status.toString()
//                                )
//                                view.validateError(responseData.message)
//                            }
                        }
                    }
                }, { error ->
                    view.hideProgressbar()
                    view.validateError(error.message.toString())
                })
        } else {
            view.hideProgressbar()
            view.validateError(context!!.getString(R.string.check_internet))
        }
    }


}
package com.tekzee.amiggos.ui.login

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginPresenterImplementation(
    private var mainView: LoginPresenter.LoginMainView,
    context: Context
) : LoginPresenter.LoginMainPresenter {

    var context: Context? = context
    private var disposable: Disposable? = null

    override fun login(input: JsonObject) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.login(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: LoginResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            when (responseData!!.status) {
                                0 ->
                                    mainView.validateError(responseData.message)

                                1 -> mainView.onLoginSuccess(responseData)

                            }


                        }
                        500->{
                            mainView.validateError(context!!.getString(R.string.error_message))
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(context!!.getString(R.string.error_message))
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
package com.ants.driverpartner.everywhere.activity.vehicleInfo

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.activity.vehicleInfo.model.VehicleCategory
import com.ants.driverpartner.everywhere.utils.Utility
import com.tekzee.mallortaxi.network.ApiClient
import com.tekzee.mallortaxi.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class VehicleInfoPresenterImplementation(private var mainView: VehicleInfoPresenter.VehicleInfoView,context: Context):VehicleInfoPresenter.VehicleInfoMainPresenter{

    var context: Context? = context
    private var disposable: Disposable? = null

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun getVechicleCategory() {

        if(mainView.checkInternet()){
            mainView.showProgressbar()

            val user_id = RequestBody.create(MultipartBody.FORM, Utility.getSharedPreferences(mainView.getContext(),Constant.USER_ID).toString())

            var headers = HashMap<String, String?>()

            headers["api-key"] = Utility.getSharedPreferences(mainView.getContext(), Constant.API_KEY)

            disposable = ApiClient.instance.getVehicleCategory(headers, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<VehicleCategory> ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: VehicleCategory? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                                mainView.onGetVehicleCategory(responseData)
                            } else {
                                Log.e(
                                    javaClass.simpleName + "\tApi output\n\n",
                                    responseData.status.toString()
                                )
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })


        }else{
            mainView.validateError("Please Check Internet Connection!")
        }

    }

}
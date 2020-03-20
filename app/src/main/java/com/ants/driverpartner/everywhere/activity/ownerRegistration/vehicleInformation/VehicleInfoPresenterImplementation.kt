//package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation
//import android.content.Context
//import android.util.Log
//import com.ants.driverpartner.everywhere.Constant
//import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
//import com.ants.driverpartner.everywhere.utils.Utility
//import com.tekzee.mallortaxi.network.ApiClient
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Response
//
//class VehiclePresenterImplementation(
//    private var mainView: VehiclePresenter.VehicleView,
//    context: Context
//) : VehiclePresenter.VehicleMainPresenter {
//
//    var context: Context? = context
//    private var disposable: Disposable? = null
//
//    override fun onStop() {
//        if (disposable != null) {
//            disposable!!.dispose()
//        }
//    }
//
//    override fun getVechicleCategory() {
//
//        if (mainView.checkInternet()) {
//            mainView.showProgressbar()
//
//            val user_id = RequestBody.create(
//                MultipartBody.FORM,
//                Utility.getSharedPreferences(mainView.getContext(), Constant.USER_ID).toString()
//            )
//
//            var headers = HashMap<String, String?>()
//
//            headers["api-key"] =
//                Utility.getSharedPreferences(mainView.getContext(), Constant.API_KEY)
//
//            disposable = ApiClient.instance.getVehicleCategory(headers, user_id.toString())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<VehicleCategory> ->
//                    mainView.hideProgressbar()
//                    val responseCode = response.code()
//                    when (responseCode) {
//                        200 -> {
//                            val responseData: VehicleCategory? = response.body()
//                            Log.e(javaClass.simpleName, response.body().toString())
//
//                            if (responseData != null) {
//                                when (responseData.status) {
//                                    0 -> {
//                                        mainView.validateError(responseData.message)
//                                    }
//                                    1 -> {
//                                        mainView.onGetVehicleCategory(responseData)
//                                    }
//                                    2 -> {
//                                        mainView.validateError(responseData.message)
//                                    }
//                                }
//                            }else{
//                                mainView.validateError("Something went wrong!")
//                            }
//
//                        }
//                    }
//                }, { error ->
//                    mainView.hideProgressbar()
//                    mainView.validateError(error.message.toString())
//                })
//
//
//        } else {
//            mainView.validateError("Please Check Internet Connection!")
//        }
//
//    }
//
//}
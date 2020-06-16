package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.RegisterVehicleResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.utils.Utility
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response


class VehiclePresenter(private var view: VehicleView, context: Context) {


    var context: Context? = context
    private var disposable: Disposable? = null

    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun getVechicleCategory() {

        if (view.checkInternet()) {
            view.showProgressbar()

            val user_id = RequestBody.create(
                MultipartBody.FORM,
                Utility.getSharedPreferences(view.getContext(), Constant.USER_ID).toString()
            )

            var headers = HashMap<String, String?>()

            headers["api-key"] =
                Utility.getSharedPreferences(view.getContext(), Constant.API_KEY)

            disposable = ApiClient.instance.getVehicleCategory(headers, user_id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<VehicleCategory> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: VehicleCategory? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())

                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onGetVehicleCategory(responseData)
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
                    view.validateError(context!!.getString(R.string.error_message))
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }

    }

    fun callRegisterVehicleApi() = if (view.checkInternet()) {
        view.showProgressbar()


        try {


            val userId =
                Utility.getSharedPreferences(view.getContext(), Constant.USER_ID)!!.toRequestBody(
                    MultipartBody.FORM
                )

            val vehicleType = view.getVehicleType().toRequestBody(MultipartBody.FORM)
            val registrationDate = view.getRegistrationDate().toRequestBody(MultipartBody.FORM)
            val tare = view.getTare().toRequestBody(MultipartBody.FORM)
            val grossVehicleMass = view.getVehicleMass().toRequestBody(MultipartBody.FORM)
            val vehicleRegistrationNumber =
                view.getRegistrationNumber().toRequestBody(MultipartBody.FORM)
            val vehicleModel = view.getModel().toRequestBody(MultipartBody.FORM)
            val vehicleManufacturer = view.getManufacture().toRequestBody(MultipartBody.FORM)
            val vin = view.getVinNumber().toRequestBody(MultipartBody.FORM)

            val licenceImageFile =
                view.getLicenceImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val pictureVehicleLicense = MultipartBody.Part.createFormData(
                "picture_vehicle_license",
                "image_" + System.currentTimeMillis() + ".jpg",
                licenceImageFile
            )

            val odemeterImageFile =
                view.getOdometerImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val odometerImage = MultipartBody.Part.createFormData(
                "odometer_image",
                "image_" + System.currentTimeMillis() + ".jpg",
                odemeterImageFile
            )

            val insuranceImageFile =
                view.getInsuranceImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val insurancePicture = MultipartBody.Part.createFormData(
                "insurance_picture",
                "image_" + System.currentTimeMillis() + ".jpg",
                insuranceImageFile
            )

            val vehicleFrontImageFile =
                view.getVehicleFontImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val vehicleFrontImage = MultipartBody.Part.createFormData(
                "vehicle_front_image",
                "image_" + System.currentTimeMillis() + ".jpg",
                vehicleFrontImageFile
            )

            val vehicleBackImageFile =
                view.getVehicelBackImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val vehicleBackImage = MultipartBody.Part.createFormData(
                "vehicle_back_image",
                "image_" + System.currentTimeMillis() + ".jpg",
                vehicleBackImageFile
            )

            val vehicleLeftImageFile =
                view.getVehicleLeftImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val vehicleLeftImage = MultipartBody.Part.createFormData(
                "vehicle_left_image",
                "image_" + System.currentTimeMillis() + ".jpg",
                vehicleLeftImageFile
            )

            val vehicleRightImageFile =
                view.getVehicleRightImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val vehicleRightImage = MultipartBody.Part.createFormData(
                "vehicle_right_image",
                "image_" + System.currentTimeMillis() + ".jpg",
                vehicleRightImageFile
            )





        var headers = HashMap<String, String?>()

        headers["api-key"] =
            Utility.getSharedPreferences(view.getContext(), Constant.API_KEY)

        disposable = ApiClient.instance.callRegisterVehicleApi(
            headers,
            userId,
            vehicleType,
            registrationDate,
            tare,
            grossVehicleMass,
            vehicleRegistrationNumber,
            vehicleModel,
            vehicleManufacturer,
            vin,
            pictureVehicleLicense,
            odometerImage,
            insurancePicture,
            vehicleFrontImage,
            vehicleBackImage,
            vehicleLeftImage,
            vehicleRightImage

        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: Response<RegisterVehicleResponse> ->
                view.hideProgressbar()
                val responseCode = response.code()
                when (responseCode) {
                    200 -> {
                        val responseData: RegisterVehicleResponse? = response.body()
                        Log.e(javaClass.simpleName, response.body().toString())

                        if (responseData != null) {
                            when (responseData.status) {
                                0 -> {
                                    view.validateError(responseData.message)
                                }
                                1 -> {
                                    view.onRegisterSuccess(responseData)
                                }
                                2 -> {
                                    view.validateError(responseData.message)
                                }
                            }
                        } else {
                            view.validateError("Something went wrong!")
                        }

                    }
                    500 ->{
                        view.validateError(context!!.getString(R.string.error_message))
                    }
                }
            }, { error ->
                view.hideProgressbar()
                view.validateError(context!!.getString(R.string.error_message))
            })
        }catch (e:Exception){
            view.validateError(context!!.getString(R.string.error_message))

        }

    } else {
        view.validateError("Please Check Internet Connection!")
    }


//
//        interface VehicleMainPresenter{
//            fun onStop()
//            fun getVechicleCategory()
//        }
//        interface VehicleView : BaseMainView {
//            fun getContext(): Context
//            fun onGetVehicleCategory(responseData: VehicleCategory)
//        }


}
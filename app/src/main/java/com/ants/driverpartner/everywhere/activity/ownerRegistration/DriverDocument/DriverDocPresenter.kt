package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.OwnersVehilce
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.RegisterDriverResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
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

class DriverDocPresenter(private var view: DriverDocView, context: Context) {

    var context: Context = context
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
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )

            var headers = HashMap<String, String?>()

            headers["api-key"] =
                Utility.getSharedPreferences(context!!, Constant.API_KEY)

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
                                        view.onGetVehicleType(responseData)
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


    fun getOwnerVehicel() {

        if (view.checkInternet()) {
            view.showProgressbar()


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] =
                Utility.getSharedPreferences(context, Constant.API_KEY)

            disposable = ApiClient.instance.getOwnerVehicle(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<OwnersVehilce> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: OwnersVehilce? = response.body()
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
                    view.validateError(context.getString(R.string.error_message))
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }
    }

    fun registerDriver() {
        if (view.checkInternet()) {
            view.showProgressbar()

            val userId = Utility.getSharedPreferences(context, Constant.USER_ID)!!.toRequestBody(
                MultipartBody.FORM
            )

            val name = view.getName().toRequestBody(MultipartBody.FORM)
            val email = view.getEmail().toRequestBody(MultipartBody.FORM)
            val mobile = view.getMobileNumber().toRequestBody(MultipartBody.FORM)
            val residentialAddress = view.getResidentialAddress().toRequestBody(MultipartBody.FORM)
            val postalAddress = view.getPostalAddress().toRequestBody(MultipartBody.FORM)
            val vehicleId = view.getVehicle().toRequestBody(MultipartBody.FORM)
            val password = view.getPassword().toRequestBody(MultipartBody.FORM)


            val idproofFrontFile =
                view.getIdProofFrontImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val idproofFrontImage = MultipartBody.Part.createFormData(
                "idproof_front",
                "image_" + System.currentTimeMillis() + ".jpg",
                idproofFrontFile
            )

            val idproofBackFile =
                view.getIdProofBackImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val idproofBackImage = MultipartBody.Part.createFormData(
                "idproof_back",
                "image_" + System.currentTimeMillis() + ".jpg",
                idproofBackFile
            )

            val driverLicenseFrontFile =
                view.getLicenceFrontImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val driverLicenseFrontImage = MultipartBody.Part.createFormData(
                "driver_license_front",
                "image_" + System.currentTimeMillis() + ".jpg",
                driverLicenseFrontFile
            )

            val driverLicenseBackFile =
                view.getLicenceBackImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val driverLicenseBackImage = MultipartBody.Part.createFormData(
                "driver_license_back",
                "image_" + System.currentTimeMillis() + ".jpg",
                driverLicenseBackFile
            )



            val proofHomeAddFile =
                view.getHomeAddressImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val proofHomeAddImage = MultipartBody.Part.createFormData(
                "proof_home_add",
                "image_" + System.currentTimeMillis() + ".jpg",
                proofHomeAddFile
            )

            val bankLetterFile =
                view.getBankLatterImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val bankLetterImage = MultipartBody.Part.createFormData(
                "bank_letter",
                "image_" + System.currentTimeMillis() + ".jpg",
                bankLetterFile
            )

            val bankStatementFile =
                view.getBankStatementImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val bankStatementImage = MultipartBody.Part.createFormData(
                "bank_statement",
                "image_" + System.currentTimeMillis() + ".jpg",
                bankStatementFile
            )

            val profileFile =
                view.getDriverImage()!!.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val profileImage = MultipartBody.Part.createFormData(
                "profile",
                "image_" + System.currentTimeMillis() + ".jpg",
                profileFile
            )


            val proffesionalDriverFace = MultipartBody.Part.createFormData(
                "proffesional_driver_face",
                "image_" + System.currentTimeMillis() + ".jpg",
                profileFile
            )


            var headers = HashMap<String, String?>()

            headers["api-key"] =
                Utility.getSharedPreferences(context, Constant.API_KEY)
//            headers["Content-Type"] =
//                "application/json"

            disposable = ApiClient.instance.callRegisterDriverApi1(
                headers,
                userId,
                name,
                email,
                mobile,
                residentialAddress,
                postalAddress,
                vehicleId,
                password,
                idproofFrontImage,
                idproofBackImage,
                driverLicenseFrontImage,
                driverLicenseBackImage,
                proofHomeAddImage,
                bankLetterImage,
                bankStatementImage,
                profileImage,
                proffesionalDriverFace

            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<RegisterDriverResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: RegisterDriverResponse? = response.body()
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
                    }
                }, { error ->
                    view.hideProgressbar()
                    view.validateError(context.getString(R.string.error_message))
                })


        } else {
            view.validateError("Please Check Internet Connection!")
        }

    }

}

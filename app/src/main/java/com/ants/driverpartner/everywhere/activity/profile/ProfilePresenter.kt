package com.ants.driverpartner.everywhere.activity.profile

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.profile.model.GetProfileResponse
import com.ants.driverpartner.everywhere.activity.profile.model.UpdateProfileResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.JsonObject
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class ProfilePresenter(private var view: ProfileView, context: Context) {

    var context = context
    private var disposable: Disposable? = null

    fun getProfile() {
        view.showProgressbar()
        if (view.checkInternet()) {


            var input = JsonObject()

            input.addProperty(
                "userid",
                Utility.getSharedPreferences(view.getContext(), Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()


            headers["api-key"] =
                Utility.getSharedPreferences(context!!, Constant.API_KEY)

            Log.e(javaClass.simpleName, headers.toString())


            disposable = ApiClient.instance.getProfile(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetProfileResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetProfileResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                when (responseData.status) {

                                    0 -> {
                                        view.validateError(responseData.message)
                                    }

                                    1 -> {
                                        view.onGetProfile(responseData.data)
                                    }

                                    2 -> {
                                        view.validateError(responseData.message)
                                    }


                                }


                            }

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


    fun uploadProfileImage(file: File) {
        view.showProgressbar()
        if (view.checkInternet()) {

            Log.e("Internet Connection", view.checkInternet().toString())
            val type = RequestBody.create(MultipartBody.FORM, Constant.PROFILE)

            val userId = Utility.getSharedPreferences(view.getContext(), Constant.USER_ID).let {
                RequestBody.create(
                    MultipartBody.FORM,
                    it.toString()
                )
            }


            val driver_id = RequestBody.create(MultipartBody.FORM, "")

            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)

            val image = MultipartBody.Part.createFormData(
                "image",
                "image_" + System.currentTimeMillis() + ".jpg",
                requestFile
            )

            var headers = HashMap<String, String?>()


            headers["api-key"] =
                Utility.getSharedPreferences(context!!, Constant.API_KEY)

            Log.e(javaClass.simpleName, headers.toString())


            disposable = ApiClient.instance.uploadImage(headers, userId, image, type, driver_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UploadImageResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UploadImageResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                                view.onImageUploadSuccess(responseData)
                            } else {
                                Log.e(
                                    javaClass.simpleName + "\tApi output\n\n",
                                    responseData.status.toString()
                                )
                                view.validateError(responseData.message)
                            }
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

    fun updateProfile() {
        view.showProgressbar()
        if (view.checkInternet()) {


            var input = JsonObject()


            input.addProperty("name", view.getName())

            input.addProperty("mobile", view.getMobile())

            input.addProperty("email", view.getEmail())

            input.addProperty("residential_address", view.getResidentialAddress())
            input.addProperty("postal_address", view.getPostalAddress())
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(view.getContext(), Constant.USER_ID).toString()
            )


            var headers = HashMap<String, String?>()


            headers["api-key"] =
                Utility.getSharedPreferences(context!!, Constant.API_KEY)

            Log.e(javaClass.simpleName, headers.toString())


            disposable = ApiClient.instance.updateProfile(headers, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UpdateProfileResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UpdateProfileResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                when (responseData.status) {

                                    0 -> {
                                        view.validateError(responseData.message)
                                    }

                                    1 -> {
                                        view.onUpdateProfile(responseData)
                                    }

                                    2 -> {
                                        view.validateError(responseData.message)
                                    }


                                }


                            }

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
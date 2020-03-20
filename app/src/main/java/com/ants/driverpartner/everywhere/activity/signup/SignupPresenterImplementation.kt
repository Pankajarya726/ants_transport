package com.tekzee.amiggos.ui.login

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.signup.SignupPresenter
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
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

class SignupPresenterImplementation(
    private var mainView: SignupPresenter.SignupMainView,
    context: Context
) : SignupPresenter.SignupMainPresenter {


    var context: Context? = context
    private var disposable: Disposable? = null

    override fun signupApi(input: JsonObject) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {

            Log.e("Internet Connection", mainView.checkInternet().toString())

            disposable = ApiClient.instance.signup(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<RegisterResponse> ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    Log.e(javaClass.simpleName, responseCode.toString())
                    when (responseCode) {
                        200 -> {
                            val responseData: RegisterResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                                mainView.onRegisterSuccess(responseData)
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
                    Log.e(
                        javaClass.simpleName + "\tApi output\n\n",
                        "jhagdfhjgsd"
                    )
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun uploadProfileImage(file: File) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {

            Log.e("Internet Connection", mainView.checkInternet().toString())
            val type = RequestBody.create(MultipartBody.FORM, Constant.PROFILE)

            val userId = Utility.getSharedPreferences(mainView.getContext(),Constant.USER_ID).let {
                RequestBody.create(MultipartBody.FORM,
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
                Utility.getSharedPreferences(mainView.getContext(), Constant.API_KEY)

            Log.e(javaClass.simpleName, headers.toString())


            disposable = ApiClient.instance.uploadImage(headers, userId, image, type, driver_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UploadImageResponse> ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UploadImageResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                                mainView.onImageUploadSuccess(responseData)
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
package com.ants.driverpartner.everywhere.activity.signature

import android.content.Context
import android.util.Log
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.utils.Utility
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class SignaturePresenter(private var view: SignatureView, private var context: Context) {

    private var disposable: Disposable? = null
    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun uploadSignature(file: File, bookingId: String) {
        if (view.checkInternet()) {
            view.showProgressbar()

            Log.e("Internet Connection", view.checkInternet().toString())

            val userId = Utility.getSharedPreferences(context, Constant.USER_ID).let {
                it.toString()
                    .toRequestBody(MultipartBody.FORM)
            }
            val bookingId =  bookingId.toRequestBody(MultipartBody.FORM)


            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val image = MultipartBody.Part.createFormData(
                "image",
                "image_" + System.currentTimeMillis() + ".jpg",
                requestFile
            )

            var headers = HashMap<String, String?>()


            headers["api-key"] =
                Utility.getSharedPreferences(context, Constant.API_KEY)


            disposable = ApiClient.instance.uploadSignature(headers, userId, bookingId, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UploadSignatureResponse> ->
                    view.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UploadSignatureResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            if (responseData != null) {
                                when (responseData.status) {
                                    0 -> {
                                        view.validateError(responseData.message)
                                    }
                                    1 -> {
                                        view.onSignatureUploadSuccess(responseData)
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
            view.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
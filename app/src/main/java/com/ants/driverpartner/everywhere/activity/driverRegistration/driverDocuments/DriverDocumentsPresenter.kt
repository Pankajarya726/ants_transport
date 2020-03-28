package com.ants.driverpartner.everywhere.activity.driverRegistration.driverDocuments

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

class DriverDocumentsPresenter(private var mainView: DriverDocumentView,context: Context) {


    var context: Context? = context
    private var disposable: Disposable? = null


     fun uploadDocument(uploadType: String, file: File) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
            // Utility.showProgressDialog(context,"Uploading image please wait...")

            Log.e("Internet Connection", mainView.checkInternet().toString())
            val type = uploadType.toRequestBody(MultipartBody.FORM)
            val userId = Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
                .toRequestBody(MultipartBody.FORM)


            val driver_id = Utility.getSharedPreferences(context!!, Constant.USER_ID).toString().toRequestBody(MultipartBody.FORM)

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val image = MultipartBody.Part.createFormData(
                "image",
                "image_" + System.currentTimeMillis() + ".jpg",
                requestFile
            )

            var headers = HashMap<String, String?>()


            headers["api-key"] = Utility.getSharedPreferences(context!!, Constant.API_KEY)

            Log.e(javaClass.simpleName, headers.toString())


            disposable = ApiClient.instance.uploadImage(headers, userId, image, type, driver_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UploadImageResponse> ->
                    mainView.hideProgressbar()
                    // Utility.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UploadImageResponse? = response.body()
                            Log.e(javaClass.simpleName, response.body().toString())


                            when(responseData!!.status){

                                1->{
                                    mainView.onImageUploadSuccess(responseData)
                                }
                                0->
                                {
                                    mainView.validateError(responseData.message)
                                }

                                2->{
                                    mainView.validateError(responseData.message)
                                }

                            }

                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
//                    Utility.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
//            Utility.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

     fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
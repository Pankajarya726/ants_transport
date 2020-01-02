package com.ants.driverpartner.everywhere.activity.ownerDocuments

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
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class OwnerDocPresenterImplementation(private var mainView: OwnerDocPresenter.DocumentView, context: Context) :
    OwnerDocPresenter.DocumentMainPresenter {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun uploadDocument(uploadType: String, file: File) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
           // Utility.showProgressDialog(context,"Uploading image please wait...")

            Log.e("Internet Connection", mainView.checkInternet().toString())
            val type = RequestBody.create(MultipartBody.FORM, uploadType)
            val userId = RequestBody.create(MultipartBody.FORM,
                Utility.getSharedPreferences(mainView.getContext(), Constant.USER_ID).toString()
            )


//            val userId = Utility.getSharedPreferences(mainView.getContext(), Constant.USER_ID).let {
//                RequestBody.create(
//                    MultipartBody.FORM,
//                    it.toString()
//                )
//            }

            val driver_id = RequestBody.create(MultipartBody.FORM, "")

            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)

            val image = MultipartBody.Part.createFormData(
                "image",
                "image_" + System.currentTimeMillis() + ".jpg",
                requestFile
            )

            var headers = HashMap<String, String?>()


            headers["api-key"] = Utility.getSharedPreferences(mainView.getContext(), Constant.API_KEY)

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


                            if (responseData != null) {
                                Log.e(javaClass.simpleName, responseData.status.toString())
                            }

                            if (responseData!!.status == 1) {
                              //  mainView.onImageUploadSuccess(responseData)
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
//                    Utility.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
//            Utility.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
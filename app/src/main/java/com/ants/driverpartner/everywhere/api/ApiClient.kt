package com.tekzee.mallortaxi.network


import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPassResponse
import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.notification.NotificationResponse
import com.ants.driverpartner.everywhere.activity.otp.VerifyOtpResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.OwnersVehilce
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.RegisterDriverResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.RegisterVehicleResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.activity.profile.model.GetProfileResponse
import com.ants.driverpartner.everywhere.activity.profile.model.UpdateProfileResponse
import com.ants.driverpartner.everywhere.activity.signature.UploadSignatureResponse
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.activity.splash.ValidateAppResponse
import com.ants.driverpartner.everywhere.activity.vehicleDetails.GetVehicleListResponse
import com.ants.driverpartner.everywhere.activity.webView.GetWebViewResponse
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.UpdateLatLongResposse
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.BookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger.addLogAdapter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    private val apiService: ApiService

    init {
        val gson = GsonBuilder().setLenient().create()
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
        clientBuilder.addInterceptor(loggingInterceptor)
        addLogAdapter(AndroidLogAdapter())
//        if (BuildConfig.DEBUG) {
//            val loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            clientBuilder.addInterceptor(loggingInterceptor)
//            addLogAdapter(AndroidLogAdapter())
//        }

        val okHttpClient: OkHttpClient = clientBuilder
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()


        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    companion object {
        private val BASE_URL = "http://dev.tekzee.in/Ants/api/"
        private var apiClient: ApiClient? = null
        /**
         * Gets my app client.
         *
         * @return the my app client
         */
        val instance: ApiClient
            get() {
                if (apiClient == null) {
                    apiClient = ApiClient()
                }
                return apiClient as ApiClient
            }
    }

    fun signup(
        input: JsonObject
    ): Observable<Response<RegisterResponse>> {
        return apiService.signup(input)
    }

    fun login(
        input: JsonObject
    ): Observable<Response<LoginResponse>> {
        return apiService.login(input)
    }

    fun uploadImage(
        headers: HashMap<String, String?>,
        userId: RequestBody, image: MultipartBody.Part, type: RequestBody, driver_id: RequestBody
    ): Observable<Response<UploadImageResponse>> {
        return apiService.uploadImage(headers, userId, image, type, driver_id)
    }


    fun getVehicleCategory(
        headers: HashMap<String, String?>,
        userid: String
    ): Observable<Response<VehicleCategory>> {
        return apiService.getVehicleCategory(headers, userid)
    }

    fun callRegisterVehicleApi(
        headers: java.util.HashMap<String, String?>,
        userId: RequestBody,
        vehicleType: RequestBody,
        registrationDate: RequestBody,
        tare: RequestBody,
        grossVehicleMass: RequestBody,
        vehicleRegistrationNumber: RequestBody,
        vehicleModel: RequestBody,
        vehicleManufacturer: RequestBody,
        vin: RequestBody,
        pictureVehicleLicense: MultipartBody.Part,
        odometerImage: MultipartBody.Part,
        insurancePicture: MultipartBody.Part,
        vehicleFrontImage: MultipartBody.Part,
        vehicleBackImage: MultipartBody.Part,
        vehicleLeftImage: MultipartBody.Part,
        vehicleRightImage: MultipartBody.Part
    ): Observable<Response<RegisterVehicleResponse>> {
        return apiService.registerVehicleApi(
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
    }

    fun getOwnerVehicle(headers: java.util.HashMap<String, String?>, input: JsonObject):
            Observable<Response<OwnersVehilce>> {
        return apiService.getOwnerVehicle(headers, input)
    }

    fun callRegisterDriverApi(
        headers: java.util.HashMap<String, String?>,
        userId: RequestBody,
        name: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        residentialAddress: RequestBody,
        postalAddress: RequestBody,
        vehicleId: RequestBody,
        password: RequestBody,
        idproofFrontImage: MultipartBody.Part,
        idproofBackImage: MultipartBody.Part,
        driverLicenseFrontImage: MultipartBody.Part,
        driverLicenseBackImage: MultipartBody.Part,
        proofHomeAddImage: MultipartBody.Part,
        bankLetterImage: MultipartBody.Part,
        bankStatementImage: MultipartBody.Part,
        profileImage: MultipartBody.Part
    ): Observable<Response<RegisterDriverResponse>> {
        return apiService.registerDriverApi(
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
            profileImage

        )
    }

    fun callRegisterDriverApi1(
        headers: HashMap<String, String?>,
        userId: RequestBody,
        name: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        residentialAddress: RequestBody,
        postalAddress: RequestBody,
        vehicleId: RequestBody,
        password: RequestBody,
        idproofFrontImage: MultipartBody.Part,
        idproofBackImage: MultipartBody.Part,
        driverLicenseFrontImage: MultipartBody.Part,
        driverLicenseBackImage: MultipartBody.Part,
        proofHomeAddImage: MultipartBody.Part,
        bankLetterImage: MultipartBody.Part,
        bankStatementImage: MultipartBody.Part,
        profileImage: MultipartBody.Part,
        proffesionalDriverFace: MultipartBody.Part
    ): Observable<Response<RegisterDriverResponse>> {
        return apiService.registerDriverApi1(
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
    }

    fun callVehicleListApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetVehicleListResponse>> {
        return apiService.callVehicleListApi(headers, input)
    }

    fun callGetDriverListApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetDriverListResponse>> {
        return apiService.callGetDriverListApi(headers, input)
    }

    fun callGetNewBookingApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetNewBookingResponse>> {
        return apiService.callGetNewBookingApi(headers, input)
    }

    fun getHistoryBooking(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetHistroyBookingResponse>> {
        return apiService.getHistoryBooking(headers, input)
    }

    fun getProfile(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetProfileResponse>> {
        return apiService.getProfile(headers, input)
    }

    fun callAcceptBookingApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<BookingResponse>> {
        return apiService.callAcceptBookingApi(headers, input)
    }

    fun callGetScheduleBookingApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<ScheduleBookingResponse>> {
        return apiService.callGetScheduleBookingApi(headers, input)
    }

    fun loadWebPages(
        headers: HashMap<String, String?>,
        userId: RequestBody,
        type: RequestBody,
        pageId: RequestBody
    ):
            Observable<Response<GetWebViewResponse>> {
        return apiService.laodWevPages(headers, userId, type, pageId)
    }

    fun callForgotPasswordApi(
        headers: java.util.HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<ForgotPassResponse>> {
        return apiService.callForgotPasswordApi(headers, input)
    }

    fun callverifyOptApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<VerifyOtpResponse>> {
        return apiService.callverifyOptApi(headers, input)
    }

    fun callGetCurrentBookingApi(
        headers: java.util.HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<GetCurrentBookingRespone>> {
        return apiService.callGetCurrentBookingApi(headers, input)
    }

    fun callChangeBookingStatusApi(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<ChangeBookingStatusResponse>> {
        return apiService.callChangeBookingStatusApi(headers, input)
    }

    fun getNotification(
        headers: HashMap<String, String?>,
        pageno: RequestBody,
        userId: RequestBody
    ): Observable<Response<NotificationResponse>> {
        return apiService.getNotification(headers, userId, pageno)
    }

    fun validateAppVersion(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<ValidateAppResponse>> {
        return apiService.validateAppVersion(headers, input)
    }

    fun deleteNotification(
        headers: HashMap<String, String?>,
        notificationId: RequestBody,
        userId: RequestBody
    ): Observable<Response<NotificationResponse>> {
        return apiService.deleteNotification(headers, notificationId, userId)
    }

    fun callResetPasswrodApi(
        headers: java.util.HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<VerifyOtpResponse>> {
        return apiService.callResetPasswrodApi(headers, input)
    }

    fun updateProfile(
        headers: HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<UpdateProfileResponse>> {


        return apiService.updateProfile(headers, input)
    }

    fun callChangePasswrodApi(
        headers: java.util.HashMap<String, String?>,
        userId: RequestBody,
        oldPassword: RequestBody,
        newPassword: RequestBody
    ): Observable<Response<VerifyOtpResponse>> {
        return apiService.callChangePasswrodApi(headers, userId, oldPassword, newPassword)
    }

    fun updateDriverLatLong(
        headers: java.util.HashMap<String, String?>,
        json: JsonObject
    ): Observable<Response<UpdateLatLongResposse>> {
        return apiService.updateDriverLatLong(headers, json)
    }

    fun uploadSignature(
        headers: java.util.HashMap<String, String?>,
        userId: RequestBody,
        bookingId: RequestBody,
        image: MultipartBody.Part
    ): Observable<Response<UploadSignatureResponse>> {
        return apiService.uploadSignature(headers, userId, bookingId, image)
    }

    fun updateBookingStatus(
        headers: java.util.HashMap<String, String?>,
        input: JsonObject
    ): Observable<Response<UpdateLatLongResposse>> {
        return apiService.updateBookingStatus(headers, input)
    }

//    fun doValidateAppVersionApi(
//        input: JsonObject
//    ): Observable<Response<RegisterResponse>> {
//        return apiService.doValidateAppVersionApi(input, headers)
//    }
//
//


}
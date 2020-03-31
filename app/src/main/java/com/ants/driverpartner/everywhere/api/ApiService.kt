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
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.activity.splash.ValidateAppResponse
import com.ants.driverpartner.everywhere.activity.vehicleDetails.GetVehicleListResponse
import com.ants.driverpartner.everywhere.activity.webView.GetWebViewResponse
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.BookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

//  /  @POST("auth/validateAppVersion")
//    fun doValidateAppVersionApi(@Body input: JsonObject, @HeaderMap headers: HashMap<String, String?>): Observable<Response<ValidateAppVersionResponse>>


    @POST("SignUp")
    fun signup(@Body input: JsonObject): Observable<Response<RegisterResponse>>


    @Multipart
    @POST("imageUpdate")
    fun uploadImage(
        @HeaderMap createHeaders: HashMap<String, String?>,
        @Part("userid") userid: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("driver_id") driver_id: RequestBody

    ): Observable<Response<UploadImageResponse>>


    @POST("login")
    fun login(@Body input: JsonObject): Observable<Response<LoginResponse>>


    @POST("get_vehicleCategory")
    fun getVehicleCategory(@HeaderMap createHeaders: HashMap<String, String?>, @Query("userid") userid: String): Observable<Response<VehicleCategory>>

    @Multipart
    @POST("register_vehicle")
    fun registerVehicleApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Part("userid") userId: RequestBody,
        @Part("vehicle_type") vehicleType: RequestBody,
        @Part("registration_date") registrationDate: RequestBody,
        @Part("tare") tare: RequestBody,
        @Part("gross_vehicle_mass") grossVehicleMass: RequestBody,
        @Part("vehicle_registration_number") vehicleRegistrationNumber: RequestBody,
        @Part("vehicle_model") vehicleModel: RequestBody,
        @Part("vehicle_manufacturer") vehicleManufacturer: RequestBody,
        @Part("vin") vin: RequestBody,
        @Part pictureVehicleLicense: MultipartBody.Part,
        @Part odometerImage: MultipartBody.Part,
        @Part insurancePicture: MultipartBody.Part,
        @Part vehicleFrontImage: MultipartBody.Part,
        @Part vehicleBackImage: MultipartBody.Part,
        @Part vehicleLeftImage: MultipartBody.Part,
        @Part vehicleRightImage: MultipartBody.Part
    ): Observable<Response<RegisterVehicleResponse>>


    @POST("Owner_vehicle_list")
    fun getOwnerVehicle(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<OwnersVehilce>>


//    @Multipart
//    @POST("register_partner")
//    fun registerDriverApi(
//        @HeaderMap headers: HashMap<String, String?>,
//        @Part("userid") userId: RequestBody,
//        @Part("name") name: RequestBody,
//        @Part("email")  email: RequestBody,
//        @Part("mobile") mobile: RequestBody,
//        @Part("residential_address") residentialAddress: RequestBody,
//        @Part("postal_address") postalAddress: RequestBody,
//        @Part("vehicle_id") vehicleId: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part idproofFrontImage: MultipartBody.Part,
//        @Part idproofBackImage: MultipartBody.Part,
//        @Part driverLicenseFrontImage: MultipartBody.Part,
//        @Part driverLicenseBackImage: MultipartBody.Part,
//        @Part proofHomeAddImage: MultipartBody.Part,
//        @Part bankLetterImage: MultipartBody.Part,
//        @Part bankStatementImage: MultipartBody.Part,
//        @Part profileImage: MultipartBody.Part
//    ): Observable<Response<RegisterDriverResponse>>


    @Multipart
    @POST("register_vehicle")
    fun registerDriverApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Part("userid") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("residential_address") residentialAddress: RequestBody,
        @Part("postal_address") postalAddress: RequestBody,
        @Part("vehicle_id") vehicleId: RequestBody,
        @Part("password") password: RequestBody,
        @Part idproofFrontImage: MultipartBody.Part,
        @Part idproofBackImage: MultipartBody.Part,
        @Part driverLicenseFrontImage: MultipartBody.Part,
        @Part driverLicenseBackImage: MultipartBody.Part,
        @Part proofHomeAddImage: MultipartBody.Part,
        @Part bankLetterImage: MultipartBody.Part,
        @Part bankStatementImage: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part
    ): Observable<Response<RegisterDriverResponse>>

    @Multipart
    @POST("register_partner")
    fun registerDriverApi1(
        @HeaderMap headers: HashMap<String, String?>,
        @Part("userid") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("residential_address") residentialAddress: RequestBody,
        @Part("postal_address") postalAddress: RequestBody,
        @Part("vehicle_id") vehicleId: RequestBody,
        @Part("password") password: RequestBody,
        @Part idproofFrontImage: MultipartBody.Part,
        @Part idproofBackImage: MultipartBody.Part,
        @Part driverLicenseFrontImage: MultipartBody.Part,
        @Part driverLicenseBackImage: MultipartBody.Part,
        @Part proofHomeAddImage: MultipartBody.Part,
        @Part bankLetterImage: MultipartBody.Part,
        @Part bankStatementImage: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part
    ): Observable<Response<RegisterDriverResponse>>

    @POST("get_vehicle_driver")
    fun callVehicleListApi(@HeaderMap headers: HashMap<String, String?>, @Body input: JsonObject):
            Observable<Response<GetVehicleListResponse>>


    @POST("get_owners_driver")
    fun callGetDriverListApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<GetDriverListResponse>>

    @POST("get_booking_request")
    fun callGetNewBookingApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<GetNewBookingResponse>>


    @POST("get_booking_history")
    fun getHistoryBooking(
        @HeaderMap createHeaders: HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<GetHistroyBookingResponse>>


    @POST("getProfile")
    fun getProfile(@HeaderMap headers: HashMap<String, String?>, @Body input: JsonObject): Observable<Response<GetProfileResponse>>


    @POST("accept_booking_request")
    fun callAcceptBookingApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<BookingResponse>>


    @POST("get_all_confirm_booking")
    fun callGetScheduleBookingApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<ScheduleBookingResponse>>


    @Multipart
    @POST("get_staticPage")
    fun laodWevPages(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Part("userid") userId: RequestBody,
        @Part("type") type: RequestBody,
        @Part("pageid") pageId: RequestBody
    ): Observable<Response<GetWebViewResponse>>


    @POST("forgot_password")
    fun callForgotPasswordApi(
        @HeaderMap headers: HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<ForgotPassResponse>>


    @POST("verifyOTP")
    fun callverifyOptApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<VerifyOtpResponse>>


    @POST("get_current_booking")
    fun callGetCurrentBookingApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<GetCurrentBookingRespone>>


    @POST("changeBookingStatus")
    fun callChangeBookingStatusApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<ChangeBookingStatusResponse>>


    @Multipart
    @POST("getAllNotification")
    fun getNotification(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Part("page_no") pageno: RequestBody,
        @Part("userid") userId: RequestBody
    ): Observable<Response<NotificationResponse>>


    @POST("validateAppVersion")
    fun validateAppVersion(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<ValidateAppResponse>>


    @Multipart
    @POST("deleteNotification")
    fun deleteNotification(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Part("notification_id") notificationId: RequestBody,
        @Part("userid") userId: RequestBody
    ): Observable<Response<NotificationResponse>>


    @POST("reset_password")
    fun callResetPasswrodApi(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<VerifyOtpResponse>>

    @POST("updateProfile")
    fun updateProfile(
        @HeaderMap headers: java.util.HashMap<String, String?>,
        @Body input: JsonObject
    ): Observable<Response<UpdateProfileResponse>>


//    @Multipart
//    @POST("user/save_photoid")
//    fun doCallAttachIdApi(
//        @Part file: MultipartBody.Part?,
//        @Part("userid") valueInt: RequestBody,
//        @Part("dob") date: RequestBody,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>>


}
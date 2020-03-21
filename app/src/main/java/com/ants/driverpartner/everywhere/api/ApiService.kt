package com.tekzee.mallortaxi.network


import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.OwnersVehilce
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.RegisterDriverResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.RegisterVehicleResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
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
        @HeaderMap  headers: java.util.HashMap<String, String?>,
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
        @Part("email")  email: RequestBody,
        @Part("mobile")  mobile: RequestBody,
        @Part("residential_address")  residentialAddress: RequestBody,
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


//    @Multipart
//    @POST("user/save_photoid")
//    fun doCallAttachIdApi(
//        @Part file: MultipartBody.Part?,
//        @Part("userid") valueInt: RequestBody,
//        @Part("dob") date: RequestBody,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>>


}
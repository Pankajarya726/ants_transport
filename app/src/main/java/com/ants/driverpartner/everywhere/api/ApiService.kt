package com.tekzee.mallortaxi.network


import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.activity.vehicleInfo.model.VehicleCategory
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
    fun getVehicleCategory(@HeaderMap createHeaders: HashMap<String, String?>,  @Part("userid") userid: RequestBody): Observable<Response<VehicleCategory>>


//    @Multipart
//    @POST("user/save_photoid")
//    fun doCallAttachIdApi(
//        @Part file: MultipartBody.Part?,
//        @Part("userid") valueInt: RequestBody,
//        @Part("dob") date: RequestBody,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>>


}
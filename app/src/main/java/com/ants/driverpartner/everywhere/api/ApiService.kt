package com.tekzee.mallortaxi.network

import com.ants.driverpartner.everywhere.activity.Signup.RegisterResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

//  /  @POST("auth/validateAppVersion")
//    fun doValidateAppVersionApi(@Body input: JsonObject, @HeaderMap headers: HashMap<String, String?>): Observable<Response<ValidateAppVersionResponse>>


    @POST("driverRegistration")
    fun signup(@Body input: JsonObject): Observable<Response<RegisterResponse>>




//    @Multipart
//    @POST("user/save_photoid")
//    fun doCallAttachIdApi(
//        @Part file: MultipartBody.Part?,
//        @Part("userid") valueInt: RequestBody,
//        @Part("dob") date: RequestBody,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>>


}
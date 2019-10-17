package com.tekzee.mallortaxi.network


import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
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
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(loggingInterceptor)
            addLogAdapter(AndroidLogAdapter())
        }

        val okHttpClient: OkHttpClient = clientBuilder
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();


        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

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
        return apiService.uploadImage(headers,userId,image,type,driver_id)
    }

//    fun doValidateAppVersionApi(
//        input: JsonObject
//    ): Observable<Response<RegisterResponse>> {
//        return apiService.doValidateAppVersionApi(input, headers)
//    }
//
//
//    fun doLanguageConstantApi(
//        headers: HashMap<String, String?>
//    ): Observable<Response<JsonObject>> {
//        return apiService.doLanguageConstantApi(headers)
//    }
//
//    fun doCallLanguageApi(
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<LanguageResponse>> {
//        return apiService.doCallLanguageApi(createHeaders)
//    }
//
//    fun doCallSettingsApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<SettingsResponse>> {
//        return apiService.doCallSettingsApi(input,createHeaders)
//    }
//
//    fun doCallHelpCenterApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<HelpCenterResponse>> {
//        return apiService.doCallHelpCenterApi(input,createHeaders)
//    }
//
//    fun doUpdateSettings(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<UpdateSettingsResponse>> {
//        return apiService.doUpdateSettings(input,createHeaders)
//    }
//
//    fun doLoginApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<LoginResponse>> {
//        return apiService.doLoginApi(input, createHeaders)
//    }
//
//    fun callTurningUpApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<TurningUpResponse>> {
//        return apiService.callTurningUpApi(input, createHeaders)
//    }
//
//    fun doCallReferalApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<ReferalCodeResponse>> {
//        return apiService.doCallReferalApi(input, createHeaders)
//    }
//
//    fun doCallCheckVenueApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<VenueResponse>> {
//        return apiService.doCallCheckVenueApi(input, createHeaders)
//    }
//
//    fun doCallAgeGroupApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<AgeGroupResponse>> {
//        return apiService.doCallAgeGroupApi(input, createHeaders)
//    }
//
//
//    fun doCallOnlineFriendApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<OnlineFriendResponse>> {
//        return apiService.doCallOnlineFriendApi(input, createHeaders)
//    }
//
//    fun doCallRealFriendApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<RealFriendResponse>> {
//        return apiService.doCallRealFriendApi(input, createHeaders)
//    }
//
//    fun doGetMyStories(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<GetMyStoriesResponse>> {
//        return apiService.doGetMyStories(input, createHeaders)
//    }
//
//    fun doGetDashboardMapApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<DashboardReponse>> {
//        return apiService.doGetDashboardMapApi(input, createHeaders)
//    }
//
//    fun doUpdateFriendCount(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<UpdateFriendCountResponse>> {
//        return apiService.doUpdateFriendCount(input, createHeaders)
//    }
//
//    fun doCallGetSettings(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<MyPreferenceResponse>> {
//        return apiService.doCallGetSettings(input, createHeaders)
//    }
//
//    fun doCallPartyInviteApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<PartyInvitesResponse>> {
//        return apiService.doCallPartyInviteApi(input, createHeaders)
//    }
//
//
//    fun doCallInvitationApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<InvitationResponse>> {
//        return apiService.doCallInvitationApi(input, createHeaders)
//    }
//
//
//    fun doAcceptInvitationApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>> {
//        return apiService.doAcceptInvitationApi(input, createHeaders)
//    }
//
//
//    fun doRejectInvitationApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>> {
//        return apiService.doRejectInvitationApi(input, createHeaders)
//    }
//
//
//    fun doCallGuestListApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<GuestListResponse>> {
//        return apiService.doCallGuestListApi(input, createHeaders)
//    }
//
//    fun doCallGetFriendProfileApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<FriendProfileResponse>> {
//        return apiService.doCallGetFriendProfileApi(input, createHeaders)
//    }
//
//    fun doCallGetNotification(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<OnlineFriendResponse>> {
//        return apiService.doCallGetNotification(input, createHeaders)
//    }
//
//    fun doCallJoinPartyInvites(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>> {
//        return apiService.doCallJoinPartyInvites(input, createHeaders)
//    }
//
//    fun doCallDeclinePartyInvites(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>> {
//        return apiService.doCallDeclinePartyInvites(input, createHeaders)
//    }
//
//    fun docallPastPartyApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<PastPartyResponse>> {
//        return apiService.docallPastPartyApi(input, createHeaders)
//    }
//
//    fun doCallUpcomingPartyApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<PastPartyResponse>> {
//        return apiService.doCallUpcomingPartyApi(input, createHeaders)
//    }
//
//    fun docallSaveSettings(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<PreferenceSavedResponse>> {
//        return apiService.docallSaveSettings(input, createHeaders)
//    }
//
//    fun doGetVenueApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<com.tekzee.amiggos.ui.home.model.VenueResponse>> {
//        return apiService.doGetVenueApi(input, createHeaders)
//    }
//
//    fun doCallAttachIdApi(
//        file: MultipartBody.Part?,
//        valueInt: RequestBody,
//        date: RequestBody,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>> {
//        return apiService.doCallAttachIdApi(file, valueInt, date, createHeaders)
//    }


}
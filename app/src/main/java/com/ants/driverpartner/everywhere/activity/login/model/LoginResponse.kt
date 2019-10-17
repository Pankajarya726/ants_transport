package com.ants.driverpartner.everywhere.activity.login.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("account_type")
        val accountType: Int = 0,
        @SerializedName("api_token")
        val apiToken: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("mobile")
        val mobile: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("profile")
        val profile: String = "",
        @SerializedName("profile_image")
        val profileImage: String = "",
        @SerializedName("stoken")
        val stoken: String = "",
        @SerializedName("type")
        val type: Int = 0,
        @SerializedName("userid")
        val userid: Int = 0,
        @SerializedName("account_status")
        val account_status: Int = 0
    )
}
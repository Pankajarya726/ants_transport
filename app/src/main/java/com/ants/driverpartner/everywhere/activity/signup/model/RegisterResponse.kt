package com.ants.driverpartner.everywhere.activity.signup.model


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
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
        @SerializedName("device_token")
        val deviceToken: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("is_registered")
        val isRegistered: Int = 0,
        @SerializedName("mobile")
        val mobile: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("password")
        val password: String = "",
        @SerializedName("postal_address")
        val postalAddress: String = "",
        @SerializedName("residential_address")
        val residentialAddress: String = "",
        @SerializedName("stoken")
        val stoken: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("userid")
        val userid: String = ""
    )
}
package com.ants.driverpartner.everywhere.activity.login.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("account_type")
        var accountType: Int = 0,
        @SerializedName("driver_status")
        var driverStatus: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("profile_image")
        var profileImage: String = "",
        @SerializedName("stoken")
        var stoken: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("userid")
        var userid: Int = 0
    )
}
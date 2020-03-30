package com.ants.driverpartner.everywhere.activity.forgotPass


import com.google.gson.annotations.SerializedName

data class ForgotPassResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("otp")
        var otp: String = "",
        @SerializedName("userid")
        var userid: Int = 0
    )
}
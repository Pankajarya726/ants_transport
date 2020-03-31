package com.ants.driverpartner.everywhere.activity.otp

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(

    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
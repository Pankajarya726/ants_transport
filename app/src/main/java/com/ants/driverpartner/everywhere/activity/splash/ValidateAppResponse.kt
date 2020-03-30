package com.ants.driverpartner.everywhere.activity.splash


import com.google.gson.annotations.SerializedName

data class ValidateAppResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("account_type")
        var accountType: String = "",
        @SerializedName("driver_id")
        var driverId: String = "",
        @SerializedName("isMandatory")
        var isMandatory: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("url")
        var url: String = ""
    )
}
package com.ants.driverpartner.everywhere.activity.signup.model


import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("url")
        val url: String = ""
    )
}
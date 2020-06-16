package com.ants.driverpartner.everywhere.activity.signature


import com.google.gson.annotations.SerializedName

data class UploadSignatureResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
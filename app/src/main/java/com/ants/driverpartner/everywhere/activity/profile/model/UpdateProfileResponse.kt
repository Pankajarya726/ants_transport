package com.ants.driverpartner.everywhere.activity.profile.model


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
package com.ants.driverpartner.everywhere.activity.notification


import com.google.gson.annotations.SerializedName

data class DeleteNotification(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
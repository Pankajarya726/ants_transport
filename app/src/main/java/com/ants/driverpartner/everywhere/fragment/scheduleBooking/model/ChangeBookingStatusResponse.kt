package com.ants.driverpartner.everywhere.fragment.scheduleBooking.model


import com.google.gson.annotations.SerializedName

data class ChangeBookingStatusResponse(

    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
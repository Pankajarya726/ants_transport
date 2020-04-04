package com.ants.driverpartner.everywhere.fragment.currentBooking.model


import com.google.gson.annotations.SerializedName

data class UpdateLatLongResposse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)
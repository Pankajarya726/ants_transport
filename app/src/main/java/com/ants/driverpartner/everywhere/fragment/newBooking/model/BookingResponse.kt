package com.ants.driverpartner.everywhere.fragment.newBooking.model


import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    class Data(
    )
}
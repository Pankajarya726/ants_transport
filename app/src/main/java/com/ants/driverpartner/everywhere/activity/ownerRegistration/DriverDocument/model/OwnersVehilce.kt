package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model


import com.google.gson.annotations.SerializedName

data class OwnersVehilce(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("registration_number")
        var registrationNumber: String = ""
    )
}
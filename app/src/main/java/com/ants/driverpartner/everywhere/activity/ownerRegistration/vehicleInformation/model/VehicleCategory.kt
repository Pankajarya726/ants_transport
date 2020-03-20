package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model

import com.google.gson.annotations.SerializedName

data class VehicleCategory(
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
        @SerializedName("name")
        var name: String = ""
    )
}
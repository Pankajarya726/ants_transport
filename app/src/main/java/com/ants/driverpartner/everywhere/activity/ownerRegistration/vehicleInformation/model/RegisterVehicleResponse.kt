package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model


import com.google.gson.annotations.SerializedName

data class RegisterVehicleResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("userid")
        var userid: String = "",
        @SerializedName("vehicle_id")
        var vehicleId: String = ""
    )
}
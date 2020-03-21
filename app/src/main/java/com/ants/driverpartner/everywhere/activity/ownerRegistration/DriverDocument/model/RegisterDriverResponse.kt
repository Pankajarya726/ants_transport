package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model


import com.google.gson.annotations.SerializedName

data class RegisterDriverResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("account_type")
        var accountType: String = "",
        @SerializedName("driverid")
        var driverid: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("is_registered")
        var isRegistered: Int = 0,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("owner_id")
        var ownerId: String = "",
        @SerializedName("password")
        var password: String = "",
        @SerializedName("postal_address")
        var postalAddress: String = "",
        @SerializedName("residential_address")
        var residentialAddress: String = "",
        @SerializedName("stoken")
        var stoken: String = "",
        @SerializedName("vehicle_id")
        var vehicleId: String = ""
    )
}
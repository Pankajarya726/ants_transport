package com.ants.driverpartner.everywhere.activity.profile.model


import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("driver_id")
        var driverId: Int = 0,
        @SerializedName("email")
        var email: String = "",
        @SerializedName("gross_veh_mass")
        var grossVehMass: Any? = Any(),
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("postal_address")
        var postalAddress: String = "",
        @SerializedName("profile_image")
        var profileImage: String = "",
        @SerializedName("registration_date")
        var registrationDate: Any? = Any(),
        @SerializedName("registration_number")
        var registrationNumber: Any? = Any(),
        @SerializedName("residential_address")
        var residentialAddress: String = "",
        @SerializedName("tare")
        var tare: Any? = Any(),
        @SerializedName("vehicle_category")
        var vehicleCategory: Any? = Any(),
        @SerializedName("vehicle_front_image")
        var vehicleFrontImage: String = ""
    )
}
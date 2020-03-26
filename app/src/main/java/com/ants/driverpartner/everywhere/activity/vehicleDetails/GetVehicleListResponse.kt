package com.ants.driverpartner.everywhere.activity.vehicleDetails


import com.google.gson.annotations.SerializedName

data class GetVehicleListResponse(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("gross_veh_mass")
        var grossVehMass: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("model")
        var model: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("postal_address")
        var postalAddress: String = "",
        @SerializedName("profile_image")
        var profileImage: String = "",
        @SerializedName("registration_date")
        var registrationDate: String = "",
        @SerializedName("registration_number")
        var registrationNumber: String = "",
        @SerializedName("residential_address")
        var residentialAddress: String = "",
        @SerializedName("tare")
        var tare: String = "",
        @SerializedName("vehicle_front_image")
        var vehicleFrontImage: String = "",
        @SerializedName("vehicle_identification_number")
        var vehicleIdentificationNumber: String = "",
        @SerializedName("vehicle_type")
        var vehicleType: String = ""
    )
}
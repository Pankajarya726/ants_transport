package com.ants.driverpartner.everywhere.fragment.scheduleBooking.model


import com.google.gson.annotations.SerializedName

data class ScheduleBookingResponse(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("booking_id")
        var bookingId: Int = 0,
        @SerializedName("is_cancelled_driver")
        var isCancelledDriver: Int = 0,
        @SerializedName("package_detail")
        var packageDetail: PackageDetail = PackageDetail(),
        @SerializedName("package_id")
        var packageId: Int = 0
    ) {
        data class PackageDetail(
            @SerializedName("currency")
            var currency: String = "",
            @SerializedName("cust_image")
            var custImage: String = "",
            @SerializedName("distance")
            var distance: Double = 0.0,
            @SerializedName("distance_unit")
            var distanceUnit: String = "",
            @SerializedName("final_amount")
            var finalAmount: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("item_count")
            var itemCount: Int = 0,
            @SerializedName("item_detail")
            var itemDetail: List<ItemDetail> = listOf(),
            @SerializedName("load_type")
            var loadType: String = "",
            @SerializedName("receiver_address_line_1")
            var receiverAddressLine1: String = "",
            @SerializedName("receiver_address_line_2")
            var receiverAddressLine2: String = "",
            @SerializedName("receiver_city")
            var receiverCity: String = "",
            @SerializedName("receiver_code")
            var receiverCode: Int = 0,
            @SerializedName("receiver_email")
            var receiverEmail: String = "",
            @SerializedName("receiver_floor")
            var receiverFloor: String = "",
            @SerializedName("receiver_forkift")
            var receiverForkift: Int = 0,
            @SerializedName("receiver_lat")
            var receiverLat: String = "",
            @SerializedName("receiver_lift")
            var receiverLift: Int = 0,
            @SerializedName("receiver_long")
            var receiverLong: String = "",
            @SerializedName("receiver_name")
            var receiverName: String = "",
            @SerializedName("receiver_number")
            var receiverNumber: String = "",
            @SerializedName("receiver_phone")
            var receiverPhone: String = "",
            @SerializedName("receiver_street_name")
            var receiverStreetName: String = "",
            @SerializedName("receiver_suburb")
            var receiverSuburb: String = "",
            @SerializedName("sender_address_line_1")
            var senderAddressLine1: String = "",
            @SerializedName("sender_address_line_2")
            var senderAddressLine2: String = "",
            @SerializedName("sender_area")
            var senderArea: String = "",
            @SerializedName("sender_city")
            var senderCity: String = "",
            @SerializedName("sender_date")
            var senderDate: String = "",
            @SerializedName("sender_email")
            var senderEmail: String = "",
            @SerializedName("sender_floor")
            var senderFloor: String = "",
            @SerializedName("sender_lat")
            var senderLat: String = "",
            @SerializedName("sender_lift")
            var senderLift: Int = 0,
            @SerializedName("sender_long")
            var senderLong: String = "",
            @SerializedName("sender_name")
            var senderName: String = "",
            @SerializedName("sender_number")
            var senderNumber: String = "",
            @SerializedName("sender_phone")
            var senderPhone: String = "",
            @SerializedName("sender_street_name")
            var senderStreetName: String = "",
            @SerializedName("sender_suburb")
            var senderSuburb: String = "",
            @SerializedName("sender_supply_forkift")
            var senderSupplyForkift: Int = 0,
            @SerializedName("time")
            var time: String = "",
            @SerializedName("total_weight")
            var totalWeight: Int = 0,
            @SerializedName("userid")
            var userid: Int = 0,
            @SerializedName("vehicle_name")
            var vehicleName: String = "",
            @SerializedName("vehicle_registration_number")
            var vehicleRegistrationNumber: String = "",
            @SerializedName("vehicle_type")
            var vehicleType: Int = 0,
            @SerializedName("weight_unit")
            var weightUnit: String = ""
        ) {
            data class ItemDetail(
                @SerializedName("aditional_info")
                var aditionalInfo: String = "",
                @SerializedName("aditonal_insurance")
                var aditonalInsurance: Int = 0,
                @SerializedName("breadth")
                var breadth: String = "",
                @SerializedName("call_before_delivery")
                var callBeforeDelivery: Int = 0,
                @SerializedName("collection_lift_gate_service")
                var collectionLiftGateService: Int = 0,
                @SerializedName("collection_services")
                var collectionServices: Int = 0,
                @SerializedName("collection_weight_facility_collection")
                var collectionWeightFacilityCollection: Int = 0,
                @SerializedName("delivery_appointment_required")
                var deliveryAppointmentRequired: Int = 0,
                @SerializedName("delivery_lift_gate_service")
                var deliveryLiftGateService: Int = 0,
                @SerializedName("delivery_services")
                var deliveryServices: Int = 0,
                @SerializedName("delivery_weight_facility_collection")
                var deliveryWeightFacilityCollection: Int = 0,
                @SerializedName("height")
                var height: String = "",
                @SerializedName("is_hazardeous_material")
                var isHazardeousMaterial: Int = 0,
                @SerializedName("item_detail")
                var itemDetail: String = "",
                @SerializedName("item_image")
                var itemImage: List<Any> = listOf(),
                @SerializedName("items_stckable")
                var itemsStckable: Int = 0,
                @SerializedName("length")
                var length: String = "",
                @SerializedName("loaders")
                var loaders: String = "",
                @SerializedName("no_units")
                var noUnits: Int = 0,
                @SerializedName("package_id")
                var packageId: Int = 0,
                @SerializedName("packaging_type")
                var packagingType: String = "",
                @SerializedName("vehicle_category")
                var vehicleCategory: Any? = Any(),
                @SerializedName("weight")
                var weight: Int = 0,
                @SerializedName("what_like_delivered")
                var whatLikeDelivered: String = ""
            )
        }
    }
}
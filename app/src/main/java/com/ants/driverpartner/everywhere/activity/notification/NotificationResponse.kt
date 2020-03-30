package com.ants.driverpartner.everywhere.activity.notification


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
) {
    data class Data(
        @SerializedName("notification_list")
        var notificationList: List<Notification> = listOf(),
        @SerializedName("unreaded_count")
        var unreadedCount: Int = 0
    ) {
        data class Notification(
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("data")
            var `data`: String = "",
            @SerializedName("device_type")
            var deviceType: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("image")
            var image: String = "",
            @SerializedName("is_read")
            var isRead: Int = 0,
            @SerializedName("message")
            var message: String = "",
            @SerializedName("notification_key")
            var notificationKey: Int = 0,
            @SerializedName("subject")
            var subject: String = ""
        )
    }
}
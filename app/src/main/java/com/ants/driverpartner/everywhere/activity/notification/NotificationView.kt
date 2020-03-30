package com.ants.driverpartner.everywhere.activity.notification

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface NotificationView:BaseMainView {
    fun onGetNotification(responseData: NotificationResponse)
    fun onRemoveNotification(responseData: NotificationResponse)
}
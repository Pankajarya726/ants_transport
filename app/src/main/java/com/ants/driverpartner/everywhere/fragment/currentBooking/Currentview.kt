package com.ants.driverpartner.everywhere.fragment.currentBooking

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse

interface Currentview:BaseMainView {
    fun onGetCurrentBooking(responseData: GetCurrentBookingRespone)
    fun onStatusChange(responseData: ChangeBookingStatusResponse)
}
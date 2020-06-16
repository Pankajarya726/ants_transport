package com.ants.driverpartner.everywhere.activity.NavigationActivity

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse

interface NavigationView :BaseMainView{

    fun onGetCurrentBooking(responseData: GetCurrentBookingRespone)
    fun onStatusChange(
        responseData: ChangeBookingStatusResponse,
        bookingStatus: Int
    )

    fun onUpdateStatus(message: String)
}
package com.ants.driverpartner.everywhere.fragment.scheduleBooking

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse

interface ScheduleView:BaseMainView {
    fun onGetScheduleBooking(responseData: ScheduleBookingResponse)
    fun onStatusChange(responseData: ChangeBookingStatusResponse)
}
package com.ants.driverpartner.everywhere.fragment.newBooking

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.fragment.newBooking.model.BookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse

interface NewBookingView :BaseMainView {
    fun onGetNewBooking(responseData: GetNewBookingResponse)
    fun onAcceptBooking(responseData: BookingResponse)
    fun onDeclineBooking(
        responseData: BookingResponse,
        position: Int
    )
}
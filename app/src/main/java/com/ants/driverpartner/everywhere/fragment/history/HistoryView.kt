package com.ants.driverpartner.everywhere.fragment.history

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse

interface HistoryView:BaseMainView {
    fun onGetBookingHistory(data: List<GetHistroyBookingResponse.Data>)
}
package com.ants.driverpartner.everywhere.activity.driverDetails

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse

interface DriverListView:BaseMainView {
    fun onGetDriverList(responseData: GetDriverListResponse)
}
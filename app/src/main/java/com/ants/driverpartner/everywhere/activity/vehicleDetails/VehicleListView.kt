package com.ants.driverpartner.everywhere.activity.vehicleDetails

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface VehicleListView :BaseMainView{
    fun onGetVehicleList(responseData: GetVehicleListResponse)


}
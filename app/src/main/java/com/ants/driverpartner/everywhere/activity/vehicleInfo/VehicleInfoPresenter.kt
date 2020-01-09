package com.ants.driverpartner.everywhere.activity.vehicleInfo

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.vehicleInfo.model.VehicleCategory
import java.io.File

class VehicleInfoPresenter {
    interface VehicleInfoMainPresenter{

        fun onStop()
        fun getVechicleCategory()
    }
    interface VehicleInfoView : BaseMainView {
        fun getContext(): Context
        fun onGetVehicleCategory(responseData: VehicleCategory)

    }

}
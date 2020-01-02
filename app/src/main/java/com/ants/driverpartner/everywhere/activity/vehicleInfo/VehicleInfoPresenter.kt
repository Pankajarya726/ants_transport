package com.ants.driverpartner.everywhere.activity.vehicleInfo

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import java.io.File

class VehicleInfoPresenter {
    interface VehicleInfoMainPresenter{
        fun uploadDocument(idFront: String, fileIdFront: File)
        fun onStop()
    }
    interface VehicleInfoView : BaseMainView {
        fun getContext(): Context

    }

}
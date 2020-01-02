package com.ants.driverpartner.everywhere.activity.vehicleInfo

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import java.io.File

class VehicleInfoPresenterImplementation(private var mainView: VehicleInfoPresenter.VehicleInfoView,context: Context):VehicleInfoPresenter.VehicleInfoMainPresenter{
    override fun uploadDocument(idFront: String, fileIdFront: File) {

    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
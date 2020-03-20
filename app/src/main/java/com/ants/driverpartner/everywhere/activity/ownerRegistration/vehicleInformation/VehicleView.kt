package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.RegisterVehicleResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import java.io.File

interface VehicleView : BaseMainView {
    fun getContext(): Context
    fun onGetVehicleCategory(responseData: VehicleCategory)


    fun getVehicleType(): String
    fun getRegistrationDate(): String
    fun getTare(): String
    fun getVehicleMass(): String
    fun getRegistrationNumber(): String
    fun getModel(): String
    fun getManufacture(): String
    fun getLicenceImage(): File?
    fun getOdometerImage(): File?
    fun getInsuranceImage(): File?
    fun getVehicleFontImage(): File?
    fun getVehicelBackImage(): File?
    fun getVehicleLeftImage(): File?
    fun getVehicleRightImage(): File?
    fun getVinNumber(): String
    fun onRegisterSuccess(responseData: RegisterVehicleResponse)


}
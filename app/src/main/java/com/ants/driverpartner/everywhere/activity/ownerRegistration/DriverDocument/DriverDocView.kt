package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.OwnersVehilce
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.RegisterDriverResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import java.io.File

interface DriverDocView: BaseMainView {

    fun getName():String
    fun getEmail(): String
    fun getMobileNumber(): String
    fun getPassword(): String
    fun getResidentialAddress(): String
    fun getPostalAddress(): String
    fun getVehicle(): String
    fun getIdProofFrontImage(): File?
    fun getIdProofBackImage(): File?
    fun getLicenceFrontImage(): File?
    fun getLicenceBackImage(): File?
    fun getHomeAddressImage(): File?
    fun getOwnershipImage(): File?
    fun getBankLatterImage(): File?
    fun getBankStatementImage(): File?
    fun getDriverImage(): File?
    fun onGetVehicleCategory(responseData: OwnersVehilce)
    fun onRegisterSuccess(responseData: RegisterDriverResponse)
    fun onGetVehicleType(responseData: VehicleCategory)


}
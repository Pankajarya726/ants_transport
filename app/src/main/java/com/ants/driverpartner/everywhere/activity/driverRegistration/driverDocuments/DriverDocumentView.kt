package com.ants.driverpartner.everywhere.activity.driverRegistration.driverDocuments

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import java.io.File

interface DriverDocumentView:BaseMainView {

    fun onImageUploadSuccess(responseData: UploadImageResponse)
}
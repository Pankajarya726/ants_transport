package com.ants.driverpartner.everywhere.activity.ownerRegistration.ownerDocuments

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import java.io.File

class OwnerDocPresenter {

    interface DocumentMainPresenter{
        fun uploadDocument(idFront: String, fileIdFront: File)
        fun onStop()
    }
    interface DocumentView :BaseMainView{
        fun getContext(): Context
        fun onImageUploadSuccess(responseData: UploadImageResponse)

    }

}
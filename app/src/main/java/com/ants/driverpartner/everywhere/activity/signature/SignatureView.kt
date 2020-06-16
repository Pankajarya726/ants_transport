package com.ants.driverpartner.everywhere.activity.signature

import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.base.BaseMainActivity

interface SignatureView :BaseMainView {
    fun onSignatureUploadSuccess(responseData: UploadSignatureResponse)


}
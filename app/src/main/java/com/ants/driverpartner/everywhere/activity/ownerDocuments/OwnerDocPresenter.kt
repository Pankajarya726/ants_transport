package com.ants.driverpartner.everywhere.activity.ownerDocuments

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import java.io.File

class OwnerDocPresenter {

    interface DocumentMainPresenter{
        fun uploadDocument(idFront: String, fileIdFront: File)
        fun onStop()
    }
    interface DocumentView :BaseMainView{
        fun getContext(): Context

    }

}
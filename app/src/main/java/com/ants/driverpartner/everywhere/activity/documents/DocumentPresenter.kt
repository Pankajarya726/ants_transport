package com.ants.driverpartner.everywhere.activity.documents

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import java.io.File

class DocumentPresenter {

    interface DocumentMainPresenter{
        fun uploadDocument(idFront: String, fileIdFront: File)
        fun onStop()
    }
    interface DocumentView :BaseMainView{
        fun getContext(): Context

    }

}
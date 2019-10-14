package com.ants.driverpartner.everywhere.activity.base

import android.app.ProgressDialog
import android.widget.EditText
import com.ants.driverpartner.everywhere.Constant


interface BaseMainView : Constant {


        fun showProgressbar()
        fun hideProgressbar()
        fun hideKeyboard()

        fun checkInternet(): Boolean
        fun validateError(message: String)


}

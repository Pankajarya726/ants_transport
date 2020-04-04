package com.ants.driverpartner.everywhere.activity.base

import com.ants.driverpartner.everywhere.Constant


interface BaseMainView : Constant {


        fun showProgressbar()
        fun hideProgressbar()
        fun hideKeyboard()

        fun checkInternet(): Boolean
        fun validateError(message: String)

//        fun logout(message: String)

}

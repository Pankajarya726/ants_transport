package com.ants.driverpartner.everywhere.activity.base

import android.app.ProgressDialog
import android.widget.EditText
import com.ants.driverpartner.everywhere.Constant


interface BaseView : Constant {
    fun isNetworkConnected(): Boolean
    fun showProgressDialog(cancelable: Boolean): ProgressDialog
    fun showProgressDialog(message: CharSequence, cancelable: Boolean): ProgressDialog
    fun dismissProgressDialog()
    fun snackBarBottom(view_id: Int, message: String)
    fun snackBarTop(view_id: Int, message: String)
    fun hideSoftKeyboard()
    fun showSoftKeyboard(view: EditText)

    fun onToast(message: String)

}

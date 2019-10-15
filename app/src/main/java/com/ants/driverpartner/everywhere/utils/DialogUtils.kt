package com.ants.driverpartner.everywhere.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.Rotate3dAnimation

object DialogUtils {

    fun showAlertDialog(context: Context, msg: String) {

        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(msg)
            .setConfirmText("Ok")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()

    }

}
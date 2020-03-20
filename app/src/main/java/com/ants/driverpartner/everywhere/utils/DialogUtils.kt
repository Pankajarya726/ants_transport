package com.ants.driverpartner.everywhere.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.Rotate3dAnimation
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.ants.driverpartner.everywhere.R
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.success_dialog.view.*

object DialogUtils {


    fun showAlertDialog(context: Context, message: String) {
        MaterialStyledDialog.Builder(context)
            .setIcon(R.mipmap.ant)
            .withDialogAnimation(true)
            .setCancelable(false)
            .setTitle(R.string.app_name)
            .setDescription(message)
            .setHeaderColor(R.color.colorBlack)
            .setPositiveText("OK")
            .onPositive(
                object : MaterialDialog.SingleButtonCallback {
                    override fun onClick(@NonNull dialog: MaterialDialog, @NonNull which: DialogAction) {
                        dialog.dismiss()
                    }
                })
            .show()
    }

//    fun showCustomAlertDialog(context: Context, message: String, listner: CustomDialogClick) {
//        MaterialStyledDialog.Builder(context)
//            .setIcon(R.mipmap.ant)
//            .withDialogAnimation(true)
//            .setCancelable(false)
//            .setTitle(R.string.app_name)
//            .setDescription(message)
//            .setHeaderColor(R.color.colorBlack)
//            .setPositiveText("OK")
//            .onPositive(
//                object : MaterialDialog.SingleButtonCallback {
//                    override fun onClick(@NonNull dialog: MaterialDialog, @NonNull which: DialogAction) {
//                        dialog.dismiss()
//                        listner.onOkClick()
//                    }
//                })
//            .show()
//    }


    fun showCustomAlertDialog(context: Context, message: String, listner: CustomDialogClick) {
        var builder: AlertDialog.Builder?
        var dialog: AlertDialog?

        var view = LayoutInflater.from(context).inflate(R.layout.success_dialog, null)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        view.tv_message.text = message
        view.iv_check.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.onOkClick()
        })
    }

    fun showSuccessDialog(context: Context, message: String) {


        var builder: AlertDialog.Builder?
        var dialog: AlertDialog?

        var view = LayoutInflater.from(context).inflate(R.layout.success_dialog, null)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        view.tv_message.text = message
        view.iv_check.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })



    }


    interface CustomDialogClick {
        fun onOkClick()
    }

}
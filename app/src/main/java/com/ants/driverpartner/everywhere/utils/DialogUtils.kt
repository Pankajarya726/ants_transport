package com.ants.driverpartner.everywhere.utils

import  android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.ants.driverpartner.everywhere.R
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.logout_dialog.view.*
import kotlinx.android.synthetic.main.success_dialog.view.*
import kotlinx.android.synthetic.main.success_dialog.view.tv_message

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

    fun showLogoutDialog(context: Context, message: String, listner: CustomDialogClick) {

        var builder: AlertDialog.Builder?
        var dialog: AlertDialog?

        var logoutView = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(logoutView)
        dialog = builder.create()
        dialog.show()

        logoutView.tv_message.text = message
        logoutView.tv_yes.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.onOkClick()
        })
        logoutView.tv_no.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

    }



    fun showMandetoryDialog(context: Context, message: String, listner: UpdateListener) {

        var builder: AlertDialog.Builder?
        var dialog: AlertDialog?

        var logoutView = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(logoutView)
        dialog = builder.create()
        dialog.show()

        logoutView.tv_message.text = message
        logoutView.tv_yes.text = "Update"
        logoutView.tv_yes.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.onUpdate()


        })
        logoutView.tv_no.visibility= View.GONE

    }

    fun showNonMandetoryDialog(context: Context, message: String, listner: UpdateListener) {

        var builder: AlertDialog.Builder?
        var dialog: AlertDialog?

        var logoutView = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(logoutView)
        dialog = builder.create()
        dialog.show()

        logoutView.tv_message.text = message
        logoutView.tv_yes.text = "Update Now"
        logoutView.tv_no.text = "Update Later"

        logoutView.tv_yes.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.onUpdate()
        })
        logoutView.tv_no.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.noUpdate()
        })

    }

    interface CustomDialogClick {
        fun onOkClick()
    }
    interface UpdateListener {
        fun onUpdate()
        fun noUpdate()
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
        view.layout_check.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

    }

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
        view.layout_check.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            listner.onOkClick()
        })
    }




}
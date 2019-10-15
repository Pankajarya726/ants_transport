package com.ants.driverpartner.everywhere.utils

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {

    fun snackBarBottom(view: View, message: String) {

        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        val sbView = snackbar.getView()
        val textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackbar.show()

    }

    fun snackBarTop(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val view1 = snackbar.getView()

        try {
            val params = view1.getLayoutParams() as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            view1.setLayoutParams(params)
        } catch (e: Exception) {

            try {
                val params = view1.getLayoutParams() as DrawerLayout.LayoutParams
                params.gravity = Gravity.TOP
                view1.setLayoutParams(params)
            } catch (e1: Exception) {
                val params = view1.getLayoutParams() as CoordinatorLayout.LayoutParams
                params.gravity = Gravity.TOP
                view1.setLayoutParams(params)

            }

        }


        //        View sbView = snackbar.getView();
        val textView = view1.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackbar.show()

    }

}

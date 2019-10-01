package com.ants.driverpartner.everywhere.base

import android.app.Activity
import android.app.ProgressDialog
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.BaseView
import com.ants.driverpartner.everywhere.uitl.KeyboardUtils


open class BaseActivity : AppCompatActivity(), BaseView {

    var progressDialog: ProgressDialog? = null
    lateinit var mActivity: Activity

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        mActivity = this
    }


    override fun setContentView(view: View) {
        super.setContentView(view)
        mActivity = this
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        mActivity = this

    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun showProgressDialog(cancelable: Boolean): ProgressDialog {

        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait..")
        progressDialog!!.setCancelable(cancelable)
        progressDialog!!.show()
        return progressDialog as ProgressDialog
    }

    override fun showProgressDialog(message: CharSequence, cancelable: Boolean): ProgressDialog {

        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(message)
        progressDialog!!.setCancelable(cancelable)
        progressDialog!!.show()
        return progressDialog as ProgressDialog
    }




    override fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }


    override fun snackBarBottom(view_id: Int, message: String) {
       // SnackbarUtils.snackBarBottom(findViewById(view_id), message)

    }

    override fun snackBarTop(view_id: Int, message: String) {

      //  SnackbarUtils.snackBarTop(findViewById(view_id), message)

    }
    override fun isNetworkConnected(): Boolean {
       return true
    }

    override fun hideSoftKeyboard() {
        KeyboardUtils.hideSoftInput(this)
    }

    override fun showSoftKeyboard(editText: EditText) {
        KeyboardUtils.showSoftInput(editText, this)
    }



    override fun onToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

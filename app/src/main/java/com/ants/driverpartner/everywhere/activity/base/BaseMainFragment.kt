package com.ants.driverpartner.everywhere.activity.base

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ants.driverpartner.everywhere.utils.Utility

abstract class BaseMainFragment : Fragment(), BaseMainView {

    //    var progressDialog: ProgressBar? = null
    lateinit var mActivity: Activity

    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = this.getActivity()!!
//        builder = AlertDialog.Builder(this.context!!)
//        builder!!.setCancelable(true) // if you want user to wait for some process to finish,
//        builder!!.setView(com.ants.driverpartner.everywhere.R.layout.layout_loading_dialog)
//        dialog = builder!!.create

//        progressDialog = ProgressDialog(context)
//        progressDialog!!.setMessage("Please wait...")
//        progressDialog!!.setCancelable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun showProgressbar() {

        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Please wait...")
            progressDialog!!.setCancelable(false)
        }


        progressDialog!!.show()


    }

    override fun hideProgressbar() {
        Log.e(javaClass.simpleName, "Dismiss Alert Dialog")
//        Log.e(javaClass.simpleName, dialog!!.isShowing.toString())
//        dialog!!.dismiss()

        progressDialog!!.dismiss()
    }

    override fun checkInternet(): Boolean {
        return Utility.isNetworkConnected(activity!!)
    }

    override fun hideKeyboard() {

    }

//    override fun logout(message: String) {
//
//        DialogUtils.showCustomAlertDialog(mActivity.applicationContext, message, object : DialogUtils.CustomDialogClick{
//            override fun onOkClick() {
//
//
//            }
//        })
//    }

//    override fun hideKeyboard(){
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        var view = this.currentFocus
//        if (view == null) {
//            view = View(this)
//        }
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

}
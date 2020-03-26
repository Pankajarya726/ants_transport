package com.ants.driverpartner.everywhere.activity.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ants.driverpartner.everywhere.utils.Utility

abstract class BaseMainFragment:Fragment(),BaseMainView {

    var progressDialog: ProgressBar? = null
    lateinit var mActivity: Activity

    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = this.getActivity()!!
        builder = AlertDialog.Builder(this.context!!)
        builder!!.setCancelable(false) // if you want user to wait for some process to finish,
        builder!!.setView(com.ants.driverpartner.everywhere.R.layout.layout_loading_dialog)
        dialog = builder!!.create()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun showProgressbar() {

        Log.e(javaClass.simpleName, "Showing Alert Dialog1")
        hideKeyboard()

        if (dialog == null) {
            builder = AlertDialog.Builder(this.context!!)
            builder!!.setCancelable(false) // if you want user to wait for some process to finish,
            builder!!.setView(com.ants.driverpartner.everywhere.R.layout.layout_loading_dialog)
            dialog = builder!!.create()
        }
        dialog!!.show()


//        builder = AlertDialog.Builder(mActivity)
//        builder!!.setCancelable(false) // if you want user to wait for some process to finish,
//        builder!!.setView(com.ants.everywhere.R.layout.layout_loading_dialog)
//        dialog =   builder!!.create()
//        if(dialog!=null){
//            Log.e(javaClass.simpleName, "Showing Alert Dialog")
//            dialog!!.show()
//        }


    }

    override fun hideProgressbar() {
        Log.e(javaClass.simpleName, "Dismiss Alert Dialog")
        Log.e(javaClass.simpleName, dialog!!.isShowing.toString())
        dialog!!.dismiss()
    }

    override fun checkInternet(): Boolean {
        return Utility.isNetworkConnected(activity!!)
    }

    override fun hideKeyboard() {

    }

//    override fun hideKeyboard(){
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        var view = this.currentFocus
//        if (view == null) {
//            view = View(this)
//        }
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

}
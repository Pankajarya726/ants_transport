@file:Suppress("DEPRECATION")

package com.ants.driverpartner.everywhere.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.ants.driverpartner.everywhere.Constant
import java.util.regex.Matcher
import java.util.regex.Pattern


object Utility {
    private val ANTS = "ANTS"
    private val DEVICE_TOKEN = "DEVICE_TOKEN"
    private var sentToSettings = false
    var progressDialog: ProgressDialog? = null
    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    fun setDeviceToken(context: Context, name: String, value: String) {
        val settings = context.getSharedPreferences(DEVICE_TOKEN, 0)
        val editor = settings.edit()
        editor.putString(name, value)
        editor.commit()
    }

    fun getDeviceToken(context: Context, name: String): String? {
        val settings = context.getSharedPreferences(DEVICE_TOKEN, 0)
        return settings.getString(name, "")
    }

    fun setSharedPreference(context: Context, name: String, value: String) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.putString(name, value)
        editor.commit()
    }

    fun getSharedPreferences(context: Context, name: String): String? {
        val settings = context.getSharedPreferences(ANTS, 0)
        return settings.getString(name, "")
    }

    fun setSharedPreferenceBoolean(context: Context, name: String, value: Boolean) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.putBoolean(name, value)
        editor.commit()
    }

    fun getSharedPreferencesBoolean(context: Context, name: String): Boolean {
        val settings = context.getSharedPreferences(ANTS, 0)
        return settings.getBoolean(name, false)
    }


    fun clearSharedPreference(context: Context) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.clear()
        editor.commit()
    }

//    fun createHeaders(sharedPreferences: SharedPreference?): HashMap<String, String?> {
//        val headers = HashMap<String, String?>()
//        headers["language-code"] = if(sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE).isNullOrEmpty()){"en"}else{
//            sharedPreferences.getValueString(ConstantLib.LANGUAGE_CODE)}
//        headers["api-key"] = sharedPreferences.getValueString(ConstantLib.API_TOKEN)
//        headers["device_type"] = ConstantLib.DEVICETYPE
//        return headers
//    }

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun passwordValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected

    }


    fun checkProfilePermissions(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context,
            Constant.profilePermissionsRequired[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Constant.profilePermissionsRequired[1]
        ) == PackageManager.PERMISSION_GRANTED)

    }

    fun showDialog(context: Context?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
        }

        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
    }

    fun hideDialog() {
        progressDialog!!.dismiss()
    }


    fun log(tag: String, message: String) {
        Log.e(tag, message);
    }


    fun showProgressBar(context: Context) {
        if (dialog == null) {
            builder = AlertDialog.Builder(context)
            builder!!.setCancelable(false) // if you want user to wait for some process to finish,
            builder!!.setView(com.ants.driverpartner.everywhere.R.layout.layout_loading_dialog)
            dialog = builder!!.create()
        }
        Log.e(javaClass.simpleName, "Showing Alert Dialog")

        try {
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            Log.e(javaClass.simpleName, e.localizedMessage);
        }

    }

    fun hideProgressbar() {
        Log.e(javaClass.simpleName, "Dismiss Alert Dialog")
        Log.e(javaClass.simpleName, dialog!!.isShowing.toString())

        dialog!!.dismiss()
    }

    fun setUserName(name: String, context: Context) {

        setSharedPreference(context, Constant.NAME, name)
    }

    fun setUserEmail(email: String, context: Context) {
        setSharedPreference(context, Constant.EMAIL, email)

    }

    fun setUserImage(image: String, context: Context) {
        setSharedPreference(context, Constant.PROFILE_IMAGE_URL, image)
    }
}

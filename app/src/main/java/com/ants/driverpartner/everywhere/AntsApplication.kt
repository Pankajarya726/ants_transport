package com.ants.driverpartner.everywhere

import android.app.Application
import android.util.Log
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class AntsApplication : Application() {
    private val TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        genrateFirbaseToken()
    }

    private fun genrateFirbaseToken() {


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                Log.e(TAG, token!!)
                Utility.setDeviceToken(this, Constant.D_TOKEN, token.toString())

            })

    }
}
package com.ants.driverpartner.everywhere

import android.app.Application
import android.util.Log
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId

class AntsApplication : Application() {
    private val  TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this);
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
                Utility.setSharedPreference(applicationContext,Constant.D_TOKEN,token.toString())
                // Log and toast
                //val msg =  token
              //  Log.e(TAG, msg!!)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

    }
}
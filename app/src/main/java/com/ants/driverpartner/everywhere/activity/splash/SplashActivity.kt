package com.ants.driverpartner.everywhere.activity.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.utils.DialogUtils

class SplashActivity : BaseMainActivity(),SplashView {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 5000 //3 seconds

    private var versionCode: Int = 0
    private var versionName: String = "1.0"
    private var presenter : SplashPresenter ?= null




    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter = SplashPresenter(this,this)

        //Initialize the Handler
        mDelayHandler = Handler()



        var pInfo: PackageInfo? = null
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
            versionCode = pInfo!!.versionCode
            versionName = pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }


        presenter!!.validateAppVersion(versionName)







    }

    override fun validateError(message: String) {
        DialogUtils.showSuccessDialog(this,message)
    }

    override fun OnUpdate(responseData: ValidateAppResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNoUpdate() {
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }


    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}

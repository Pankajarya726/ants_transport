package com.ants.driverpartner.everywhere.activity.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.HomeActivity
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility

class SplashActivity : BaseMainActivity(), SplashView {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private var versionCode: Int = 0
    private var versionName: String = "1.0"
    private var presenter: SplashPresenter? = null


    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if (Utility.getSharedPreferencesBoolean(this, Constant.IS_LOGIN)) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter = SplashPresenter(this, this)

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
        DialogUtils.showSuccessDialog(this, message)
    }

    override fun OnUpdate(responseData: ValidateAppResponse) {


        if(responseData.data.isMandatory=="1"){
            DialogUtils.showMandetoryDialog(this,responseData.message,object :DialogUtils.UpdateListener{
                override fun noUpdate() {

                }

                override fun onUpdate() {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.ants.driverpartner.everywhere")
                            )
                        )
                        finish()
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }


                }
            })
        }else{
            DialogUtils.showNonMandetoryDialog(this,responseData.message,object :DialogUtils.UpdateListener{
                override fun onUpdate() {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.ants.driverpartner.everywhere")
                            )
                        )
                        finish()
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }

                override fun noUpdate() {
                    mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

                }
            })
        }
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

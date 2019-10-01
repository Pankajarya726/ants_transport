package com.ants.driverpartner.everywhere.activity.forgotPass

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.activity.otp.OtpActivity
import com.ants.driverpartner.everywhere.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : MvpActivity<ForgotPasswordPresenter>(), ForgotPasswordView {


    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.btnResetPassword.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(applicationContext, OtpActivity::class.java)
            startActivity(intent)
        })

    }

    override fun createPresenter(): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(this)
    }

}

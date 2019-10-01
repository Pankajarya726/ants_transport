package com.ants.driverpartner.everywhere.activity.resetPassword

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : MvpActivity<ResetPasswordPresenter>(), ResetPasswordView {
    lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

    }

    override fun createPresenter(): ResetPasswordPresenter {
        return ResetPasswordPresenter(this)
    }

}

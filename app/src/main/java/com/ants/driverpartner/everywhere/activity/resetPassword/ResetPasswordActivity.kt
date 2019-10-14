package com.ants.driverpartner.everywhere.activity.resetPassword

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseMainActivity(), ResetPasswordPresenter.ResetPasswordView {

    lateinit var binding: ActivityResetPasswordBinding
    private var presenter: ResetPasswordPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        presenter = ResetPasswordPresenterImplementation(this, this)

    }


    override fun validateError(message: String) {


    }

}

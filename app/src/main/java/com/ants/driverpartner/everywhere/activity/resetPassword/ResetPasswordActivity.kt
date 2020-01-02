package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseMainActivity(), ResetPasswordPresenter.ResetPasswordView,
    View.OnClickListener {

    lateinit var binding: ActivityResetPasswordBinding
    private var presenter: ResetPasswordPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        presenter = ResetPasswordPresenterImplementation(this, this)


        binding.btnResetPassword.setOnClickListener(this)

    }


    override fun validateError(message: String) {


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_reset_password -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

}

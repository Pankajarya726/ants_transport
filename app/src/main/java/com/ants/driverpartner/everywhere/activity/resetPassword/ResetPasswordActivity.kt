package com.ants.driverpartner.everywhere.activity.resetPassword

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityResetPasswordBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils

class ResetPasswordActivity : BaseMainActivity(), ResetPasswordView,
    View.OnClickListener {

    lateinit var binding: ActivityResetPasswordBinding
    private var presenter: ResetPasswordPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        presenter = ResetPasswordPresenter(this, this)


        binding.btnSubmit.setOnClickListener(this)

    }


    override fun validateError(message: String) {


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                resetPassWord()
            }
        }
    }

    private fun resetPassWord() {

        if (getCurrentPassword().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Current Password")
        } else if (getNewPassword().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter the New Password")
        } else if (binding.edtConfirmPassword.text.toString().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Cunfirm Password")
        } else if (getNewPassword() != binding.edtConfirmPassword.text.toString()) {
                DialogUtils.showSuccessDialog(this, "Password not Match")
        }else {


            presenter!!.callResetPasswrodApi()
        }

    }

    override fun getCurrentPassword(): String {

        return binding.edtCurrentPassword.text.toString()
    }


    override fun getNewPassword(): String {

        return binding.edtPassword.text.toString()
    }
}

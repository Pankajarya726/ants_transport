package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.activity.otp.VerifyOtpResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityResetPasswordBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils

class ResetPasswordActivity : BaseMainActivity(), ResetPasswordView,
    View.OnClickListener {

    lateinit var binding: ActivityResetPasswordBinding
    private var presenter: ResetPasswordPresenter? = null
    private var email = ""
    private var isForgotPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        presenter = ResetPasswordPresenter(this, this)
        email = intent.getStringExtra(Constant.EMAIL)
        isForgotPass = intent.getBooleanExtra(Constant.FORGOT_PASSWORD, false)

        binding.btnSubmit.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)

    }


    override fun validateError(message: String) {
        DialogUtils.showSuccessDialog(this, message)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                resetPassWord()
            }


            R.id.img_back -> {
                onBackPressed()
            }
        }
    }

    private fun resetPassWord() {

        if (isForgotPass) {
            if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Cunfirm Password")
            } else if (getNewPassword() != binding.edtConfirmPassword.text.toString()) {
                DialogUtils.showSuccessDialog(this, "Password not Match")
            } else {
                presenter!!.callResetPasswrodApi()
            }
        } else {

            if (getCurrentPassword().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Current Password")
            } else if (getNewPassword().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter the New Password")
            } else if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Cunfirm Password")
            } else if (getNewPassword() != binding.edtConfirmPassword.text.toString()) {
                DialogUtils.showSuccessDialog(this, "Password not Match")
            } else {


                presenter!!.callResetPasswrodApi()
            }
        }


    }

    override fun getCurrentPassword(): String {

        return binding.edtCurrentPassword.text.toString()
    }


    override fun getNewPassword(): String {

        return binding.edtPassword.text.toString()
    }

    override fun getEmail(): String {

        return email
    }

    override fun onSuccess(action: VerifyOtpResponse) {


        DialogUtils.showCustomAlertDialog(
            this,
            action.message,
            object : DialogUtils.CustomDialogClick {


                override fun onOkClick() {

                    if (isForgotPass) {
                        goToLoginActivity()
                    } else {
                        onBackPressed()
                    }

                }


            })


    }

    private fun goToLoginActivity() {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}

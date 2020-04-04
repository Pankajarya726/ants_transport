package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
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


        if (isForgotPass) {
            binding.edtCurrentPassword.visibility = View.GONE
        }

        binding.btnSubmit.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.imgConfirmPassword.setOnClickListener(this)
        binding.imgPassword.setOnClickListener(this)
        binding.edtPassword.transformationMethod = PasswordTransformationMethod()
        binding.edtConfirmPassword.transformationMethod = PasswordTransformationMethod()


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


            R.id.img_password -> {
                if (binding.edtPassword.transformationMethod == null) {
                    binding.edtPassword.transformationMethod = PasswordTransformationMethod()
                    binding.imgPassword.setImageResource(R.drawable.eye_grey)

                } else {
                    binding.edtPassword.transformationMethod = null
                    binding.imgPassword.setImageResource(R.drawable.eye_green)
                }
            }

            R.id.img__confirm_password -> {
                if (binding.edtConfirmPassword.transformationMethod == null) {

                    binding.edtConfirmPassword.transformationMethod = PasswordTransformationMethod()
                    binding.imgConfirmPassword.setImageResource(R.drawable.eye_grey)

                } else {
                    binding.edtConfirmPassword.transformationMethod = null
                    binding.imgConfirmPassword.setImageResource(R.drawable.eye_green)
                }
            }

        }
    }

    private fun resetPassWord() {

        if (isForgotPass) {
            if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Confirm Password")
            } else if (!getNewPassword().equals(binding.edtConfirmPassword.text.toString())) {
                DialogUtils.showSuccessDialog(this, "Confirm Password not Match")
            } else {
                presenter!!.callResetPasswrodApi()
            }
        } else {

            if (getCurrentPassword().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Current Password")
            } else if (getNewPassword().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter the New Password")
            } else if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Please Enter Confirm Password")
            } else if (!getNewPassword().equals(binding.edtConfirmPassword.text.toString())) {
                DialogUtils.showSuccessDialog(this, "Confirm Password not Match")
            } else {
                presenter!!.callChangePasswrodApi()
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

        Log.e(javaClass.simpleName + 1, isForgotPass.toString())

        DialogUtils.showCustomAlertDialog(
            this,
            action.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {

                    Log.e(javaClass.simpleName + 1, isForgotPass.toString())


                    if (isForgotPass) {
                        Log.e(javaClass.simpleName + 2, isForgotPass.toString())
                        goToLoginActivity()
                        onBackPressed()

                    } else {
                        Log.e(javaClass.simpleName + 3, isForgotPass.toString())
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

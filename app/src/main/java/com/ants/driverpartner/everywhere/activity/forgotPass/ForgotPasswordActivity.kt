package com.ants.driverpartner.everywhere.activity.forgotPass

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.otp.OtpActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityForgotPasswordBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility

class ForgotPasswordActivity : BaseMainActivity(), ForgotPasswordView {

    lateinit var binding: ActivityForgotPasswordBinding


    private var presenter: ForgotPasswordPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        presenter = ForgotPasswordPresenter(this, this)
        binding.btnResetPassword.setOnClickListener(View.OnClickListener { v ->
            resetPassword()
        })

        binding.imgBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })



    }

    fun resetPassword() {

        if (binding.edtMobile.text.trim().toString().isEmpty()) {
            validateError("Please Enter Email id?")
        } else if (!Utility.emailValidator(binding.edtMobile.text.trim().toString())) {
            validateError("Invalid Email id!")
        } else {


            presenter!!.callForgotPasswordApi()

        }


    }


    override fun getEmail(): String {

        return binding.edtMobile.text.trim().toString()
    }

    override fun onSuccess(action: ForgotPassResponse) {
        DialogUtils.showCustomAlertDialog(
            this,
            action.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {
                    goToVerifyActivity(action)
                }
            })

    }

    fun goToVerifyActivity(action: ForgotPassResponse) {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constant.EMAIL, binding.edtMobile.text.trim().toString())
        intent.putExtra(Constant.USER_ID, action.data.userid)
        startActivity(intent)
    }


    override fun validateError(message: String) {
        DialogUtils.showSuccessDialog(this, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }


}

package com.ants.driverpartner.everywhere.activity.otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPassResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityOtpBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils

class OtpActivity : BaseMainActivity(), OtpView {


    lateinit var binding: ActivityOtpBinding

    private var presenter: OtpPresenter? = null
    private var email = ""
    private var userid = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        presenter = OtpPresenter(this, this)
        email = intent.getStringExtra(Constant.EMAIL)
        userid = intent.getIntExtra(Constant.USER_ID, 0)



        init()


    }


    fun init() {


        binding.back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        binding.btnSubmit.setOnClickListener(View.OnClickListener { v ->
            //            val intent = Intent(applicationContext, ResetPasswordActivity::class.java)
//            startActivity(intent)
//            finish()


            presenter!!.verifyOptApi()
        })



        binding.tvResentOtp.setOnClickListener(View.OnClickListener {

            presenter!!.callForgotPasswordApi()

        })


        /**
         * Pin box 1
         */
        binding.etPinBox1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(mEditable: Editable) {
                if (mEditable.length == 1) {
                    binding.etPinBox2.requestFocus()
                }

            }
        })

        /**
         * Pin box 2
         */
        binding.etPinBox2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(mEditable: Editable) {
                if (mEditable.length == 1) {
                    binding.etPinBox3.requestFocus()
                } else if (mEditable.length == 0) {
                    binding.etPinBox1.requestFocus()
                }

            }
        })

        /**
         * Pin box 3
         */
        binding.etPinBox3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(mEditable: Editable) {
                if (mEditable.length == 1) {
                    binding.etPinBox4.requestFocus()
                } else if (mEditable.length == 0) {
                    binding.etPinBox2.requestFocus()
                }
            }
        })

        /**
         * Pin box 4
         */
        binding.etPinBox4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(mEditable: Editable) {
                if (mEditable.length == 1) {
                    hideKeyboard()
                } else if (mEditable.length == 0) {
                    binding.etPinBox3.requestFocus()
                }
            }
        })


    }


    override fun getEmail(): String {
        return email
    }

    override fun getUserid(): String {
        return userid.toString()
    }

    override fun onSuccess(action: ForgotPassResponse) {

        validateError("OTP Resent Success")


    }


    override fun getOtp(): String {
        return binding.etPinBox1.text.trim().toString() + binding.etPinBox2.text.trim().toString() + binding.etPinBox3.text.trim().toString() + binding.etPinBox4.text.trim().toString();
    }


    override fun validateError(message: String) {

        DialogUtils.showSuccessDialog(this, message)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}

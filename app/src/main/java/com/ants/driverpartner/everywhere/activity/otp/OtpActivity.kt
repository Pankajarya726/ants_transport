package com.ants.driverpartner.everywhere.activity.otp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.activity.resetPassword.ResetPasswordActivity
import com.ants.driverpartner.everywhere.databinding.ActivityOtpBinding

class OtpActivity : MvpActivity<OtpPresenter>(), OptView {


    lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        binding.btnResetPassword.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(applicationContext, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
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
                    hideSoftKeyboard()
                } else if (mEditable.length == 0) {
                    binding.etPinBox3.requestFocus()
                }
            }
        })

    }

    override fun createPresenter(): OtpPresenter {
        return OtpPresenter(this)
    }
}

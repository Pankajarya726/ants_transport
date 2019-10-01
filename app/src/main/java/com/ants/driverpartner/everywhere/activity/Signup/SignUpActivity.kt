package com.ants.driverpartner.everywhere.activity.Signup

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.activity.documents.DocumentActivity
import com.ants.driverpartner.everywhere.databinding.ActivitySignupBinding


class SignUpActivity : MvpActivity<SignupPresenter>(), SignupView {
    lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)



        binding.btnNext.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(applicationContext, DocumentActivity::class.java)
            startActivity(intent)
        })

        binding.imgPassword.setOnClickListener(View.OnClickListener { v ->

            if (binding.edtPassword.transformationMethod == null) {
                binding.edtPassword.transformationMethod = PasswordTransformationMethod()
                binding.imgPassword.setImageResource(R.drawable.eye_grey)

            } else {
                binding.edtPassword.setTransformationMethod(null)
                binding.imgPassword.setImageResource(R.drawable.eye_green)
            }
        })
        binding.imgConfirmPassword.setOnClickListener(View.OnClickListener { v ->

            if (binding.edtComfirmPassword.transformationMethod == null) {
                binding.edtComfirmPassword.transformationMethod = PasswordTransformationMethod()
                binding.imgConfirmPassword.setImageResource(R.drawable.eye_grey)

            } else {
                binding.edtComfirmPassword.setTransformationMethod(null)
                binding.imgConfirmPassword.setImageResource(R.drawable.eye_green)
            }
        })
    }

    override fun createPresenter(): SignupPresenter {
        return SignupPresenter(this)
    }

}

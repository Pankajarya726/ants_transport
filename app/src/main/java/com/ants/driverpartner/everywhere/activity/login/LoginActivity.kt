package com.ants.driverpartner.everywhere.activity.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Signup.SignUpActivity
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPasswordActivity
import com.ants.driverpartner.everywhere.databinding.LoginBinding
import com.ants.driverpartner.everywhere.uitl.Utility

open class LoginActivity : MvpActivity<LoginPresenter>(), LoginView, View.OnClickListener {


    lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login)

        binding.btnSignin.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)

    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.btn_signin -> login()

            R.id.tv_signup -> showDialog()

            R.id.tv_forgot_password -> {
                val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }


        }


    }


    open fun showDialog() {

        var a = 1
        var b = 0
        var c = 0

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_profile_type)
        dialog.setTitle("Custom Dialog")

        val imageOwner = dialog.findViewById(R.id.img_owner) as ImageView
        val imagePartner = dialog.findViewById(R.id.img_partner) as ImageView
        val imageBoth = dialog.findViewById(R.id.img_both) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ok) as Button

        dialog.show()

        if (a == 1) {
            imageOwner.setImageResource(R.drawable.radio_yes)
        } else {
            imageOwner.setImageResource(R.drawable.radio_off)
        }
        if (b == 1) {
            imagePartner.setImageResource(R.drawable.radio_yes)
        } else {
            imagePartner.setImageResource(R.drawable.radio_off)
        }
        if (c == 1) {
            imageBoth.setImageResource(R.drawable.radio_yes)
        } else {
            imageBoth.setImageResource(R.drawable.radio_off)
        }


        imageOwner.setOnClickListener(View.OnClickListener { view ->
            if (a == 0) {
                imageOwner.setImageResource(R.drawable.radio_yes)
                imagePartner.setImageResource(R.drawable.radio_off)
                imageBoth.setImageResource(R.drawable.radio_off)
                a = 1;
                b = 0;
                c = 0;
            }
        })
        imagePartner.setOnClickListener(View.OnClickListener { view ->
            if (b == 0) {
                imagePartner.setImageResource(R.drawable.radio_yes)
                imageOwner.setImageResource(R.drawable.radio_off)
                imageBoth.setImageResource(R.drawable.radio_off)
                a = 0;
                b = 1;
                c = 0;
            }
        })

        imageBoth.setOnClickListener(View.OnClickListener { view ->
            if (a == 0) {
                imageBoth.setImageResource(R.drawable.radio_yes)
                imageOwner.setImageResource(R.drawable.radio_off)
                imagePartner.setImageResource(R.drawable.radio_off)
                a = 0;
                b = 0;
                c = 1;
            }
        })






        btnOk.setOnClickListener(View.OnClickListener { view ->
            dialog.dismiss()
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            //finish()

        })


    }

    private fun login() {

        if (binding.edtEmail.text.toString().trim().isEmpty()) {
            //Toast.makeText(applicationContext, "Enter Email", Toast.LENGTH_LONG).show()
            binding.edtEmail.setError("Enter Email")

        } else if (!Utility.emailValidator(binding.edtEmail.text.toString().trim())) {
            //Toast.makeText(applicationContext, "Invalid Email", Toast.LENGTH_LONG).show()
            binding.edtEmail.setError("Invalid Email")


        } else if (binding.edtPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_LONG).show()
            binding.edtPassword.setError("Enter password")
        }

    }
}

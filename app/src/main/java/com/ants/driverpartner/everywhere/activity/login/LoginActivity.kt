package com.ants.driverpartner.everywhere.activity.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPasswordActivity
import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.signup.SignUpActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.LoginBinding
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.login.LoginPresenter
import com.tekzee.amiggos.ui.login.LoginPresenterImplementation


class LoginActivity : BaseMainActivity(), LoginPresenter.LoginMainView, View.OnClickListener {

    lateinit var binding: LoginBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var presenter: LoginPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login)
        presenter = LoginPresenterImplementation(this, this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        init()


    }

    private fun init() {

        binding.btnSignin.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnGoogle.setOnClickListener(this)
        binding.imgEye.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.btn_signin -> login()

            R.id.tv_signup -> showDialog()

            R.id.tv_forgot_password -> {
                val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_google ->
                googleSignIn()

            R.id.img_eye -> {
                if (binding.edtPassword.transformationMethod == null) {
                    binding.edtPassword.transformationMethod = PasswordTransformationMethod()
                    binding.imgEye.setImageResource(R.drawable.eye_grey)

                } else {
                    binding.edtPassword.setTransformationMethod(null)
                    binding.imgEye.setImageResource(R.drawable.eye_green)
                }
            }


        }


    }

    private fun googleSignIn() {

        Log.e(localClassName, "Google sign in clicked")

        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, Constant.RC_SIGN_IN)
    }


    override fun onStart() {
        super.onStart()
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constant.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.

            Log.e(localClassName, "signInResult=" + account)
            // updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(localClassName, "signInResult:failed code=" + e.statusCode)
            //  updateUI(null)
        }

    }

    open fun showDialog() {


        var dialog = Dialog(this)
        dialog.setContentView(com.ants.driverpartner.everywhere.R.layout.dialog_profile_type)
        dialog.setTitle("Custom Dialog")

        var btnOk = dialog.findViewById(R.id.btn_ok) as Button
        var radioGroup = dialog.findViewById(R.id.radio_group_profile) as RadioGroup


        dialog.show()

        btnOk.setOnClickListener(View.OnClickListener { view ->

            var selected = dialog.findViewById(radioGroup.checkedRadioButtonId) as RadioButton
            dialog.dismiss()

            when (selected) {
                dialog.findViewById(R.id.radio_owner) as RadioButton -> {
                    val intent = Intent(applicationContext, SignUpActivity::class.java)
                    intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
                    startActivity(intent)
                }
                dialog.findViewById(R.id.radio_partner) as RadioButton -> {
                    val intent = Intent(applicationContext, SignUpActivity::class.java)
                    intent.putExtra(Constant.PROFILE_TYPE, Constant.PARTNER)
                    startActivity(intent)
                }
                dialog.findViewById(R.id.radio_both) as RadioButton -> {
                    val intent = Intent(applicationContext, SignUpActivity::class.java)
                    intent.putExtra(Constant.PROFILE_TYPE, Constant.BOTH)
                    startActivity(intent)
                }

            }


            //finish()

        })


    }


    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        } else {
            var input = JsonObject()
            input.addProperty("email", binding.edtEmail.text.toString().trim())
            input.addProperty("password", binding.edtPassword.text.toString())
            input.addProperty("device_type", 1)
            input.addProperty("device_token", "adgasdgadsg")

            presenter!!.login(input)
        }

    }


    override fun onLoginSuccess(responseData: LoginResponse) {

        when (responseData.data.account_status) {

            1 -> gotoDocumentActivity(responseData.data.accountType)

            2 -> gotoVehicleActivity(responseData.data.accountType)

            3 -> gotoDocumentActivity(responseData.data.accountType)

            4 -> gotoHomeActivity()

        }
    }

    private fun gotoHomeActivity() {

    }

    private fun gotoVehicleActivity(accountType: Int) {


    }

    private fun gotoDocumentActivity(accountType: Int) {


    }

}

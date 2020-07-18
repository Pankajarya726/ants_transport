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
import com.ants.driverpartner.everywhere.activity.Home.HomeActivity
import com.ants.driverpartner.everywhere.activity.driverRegistration.DriverDocumentsActivity
import com.ants.driverpartner.everywhere.activity.forgotPass.ForgotPasswordActivity
import com.ants.driverpartner.everywhere.activity.login.model.LoginResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.DriverDocActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.ownerDocuments.OwnerDocActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.VehicleActivity
import com.ants.driverpartner.everywhere.activity.signup.SignUpActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.LoginBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
        binding.edtPassword.transformationMethod = PasswordTransformationMethod()

        binding.btnSignin.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.imgEye.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.btn_signin -> login()

            R.id.tv_signup -> showDialog()

            R.id.tv_forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }


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


    open fun showDialog() {


        var dialog = Dialog(this)

        dialog.setContentView(com.ants.driverpartner.everywhere.R.layout.dialog_profile_type)

        var btnOk = dialog.findViewById(R.id.btn_ok) as Button
        var radioGroup = dialog.findViewById(R.id.radio_group_profile) as RadioGroup


        dialog.show()

        btnOk.setOnClickListener(View.OnClickListener { view ->

            var selected = dialog.findViewById(radioGroup.checkedRadioButtonId) as RadioButton
            dialog.dismiss()

            when (selected) {
                dialog.findViewById(R.id.radio_owner) as RadioButton -> {
                    Utility.setSharedPreference(this, Constant.ACCOUNT_TYPE, Constant.OWNER)
                    val intent = Intent(this, SignUpActivity::class.java)
                    intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
                    startActivity(intent)
                }

                dialog.findViewById(R.id.radio_partner) as RadioButton -> {
                    Utility.setSharedPreference(this, Constant.ACCOUNT_TYPE, Constant.DRIVER)
                    val intent = Intent(this, SignUpActivity::class.java)
                    intent.putExtra(Constant.PROFILE_TYPE, Constant.DRIVER)
                    startActivity(intent)
                }

            }


            //finish()

        })


    }


    override fun validateError(message: String) {

        DialogUtils.showSuccessDialog(this,message)
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    private fun login() {

        if (binding.edtEmail.text.toString().trim().isEmpty()) {
            //Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show()
            binding.edtEmail.setError("Enter Email")

        } else if (!Utility.emailValidator(binding.edtEmail.text.toString().trim())) {
            //Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show()
            binding.edtEmail.setError("Invalid Email")

        } else if (binding.edtPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show()
            binding.edtPassword.setError("Enter password")
        } else {
            var input = JsonObject()
            input.addProperty("email", binding.edtEmail.text.toString().trim())
            input.addProperty("password", binding.edtPassword.text.toString())
            input.addProperty("device_type", 2)
            input.addProperty("device_token", Utility.getDeviceToken(this, Constant.D_TOKEN))


            presenter!!.login(input)
        }

    }


    override fun onLoginSuccess(responseData: LoginResponse) {

        Log.e(javaClass.simpleName, responseData.data.driverStatus.toString())


//        Utility.setSharedPreference(this, Constant.API_KEY, responseData.data.a)
        Utility.setSharedPreference(this, Constant.USER_ID, responseData.data.userid.toString())
        Utility.setSharedPreference(this, Constant.EMAIL, responseData.data.email)
        Utility.setSharedPreference(this, Constant.MOBILE, responseData.data.mobile)
        Utility.setSharedPreference(this, Constant.NAME, responseData.data.name)
        Utility.setSharedPreference(
            this,
            Constant.PROFILE_IMAGE_URL,
            responseData.data.profileImage
        )
        Utility.setSharedPreference(this, Constant.S_TOKEN, responseData.data.stoken)
        Utility.setSharedPreference(this, Constant.API_KEY, responseData.data.stoken)


        Utility.setSharedPreference(
            this,
            Constant.PROFILE_TYPE,
            responseData.data.accountType.toString()
        )


        if (responseData.data.accountType == 1) {
            Utility.setSharedPreference(this, Constant.ACCOUNT_TYPE, Constant.OWNER)
        } else if (responseData.data.accountType == 2) {
            Utility.setSharedPreference(this, Constant.ACCOUNT_TYPE, Constant.DRIVER)
        }

        when (responseData.data.driverStatus.toString()) {

            "1" -> gotoDocumentActivity(responseData.data.accountType)

            "2" -> gotoVehicleActivity(responseData.data.accountType)

            "3" -> gotoDriverDocActivity(responseData.data.accountType)

            "4" -> gotoHomeActivity()

        }
    }

    private fun gotoDriverDocActivity(accountType: Int) {


        when (accountType) {
            1 -> {
                var intent = Intent(this, DriverDocActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
                intent.putExtra(Constant.ADDING_DRIVER, "")
                startActivity(intent)

            }
            2 -> {
                var intent = Intent(this, OwnerDocActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, Constant.DRIVER)

                startActivity(intent)
            }
        }


    }

    private fun gotoHomeActivity() {
        Utility.setSharedPreferenceBoolean(this, Constant.IS_LOGIN, true)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }

    private fun gotoVehicleActivity(accountType: Int) {
        var intent = Intent(this, VehicleActivity::class.java)

        when (accountType) {
            1 -> {
                intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
            }

            2 -> {
                intent.putExtra(Constant.PROFILE_TYPE, Constant.DRIVER)
            }
        }

        intent.putExtra(Constant.ADDING_VEHICLE, "")

        startActivity(intent)
    }

    private fun gotoDocumentActivity(accountType: Int) {


        when (accountType) {
            1 -> {

                var intent = Intent(this, OwnerDocActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
                startActivity(intent)
            }


            2 -> {
                var intent = Intent(this, DriverDocumentsActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, Constant.DRIVER)
                startActivity(intent)

            }
        }


    }


}

package com.ants.driverpartner.everywhere.activity.signup

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.Constant.Companion.PROFILE_PERMISSION_CALLBACK
import com.ants.driverpartner.everywhere.Constant.Companion.REQUEST_PERMISSION_SETTING
import com.ants.driverpartner.everywhere.Constant.Companion.profilePermissionsRequired
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverRegistration.DriverDocumentsActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.ownerDocuments.OwnerDocActivity
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivitySignupBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.DialogUtils.showCustomAlertDialog
import com.ants.driverpartner.everywhere.utils.DialogUtils.showSuccessDialog
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.tekzee.amiggos.ui.login.SignupPresenterImplementation
import java.io.File
import java.io.InputStream


class SignUpActivity : BaseMainActivity(), SignupPresenter.SignupMainView,
    BSImagePicker.OnSingleImageSelectedListener {

    lateinit var binding: ActivitySignupBinding
    var title: String = ""
    private var sentToSettings = false
    private var upload_type: String? = null
    private var presenter: SignupPresenterImplementation? = null
    private var inputStream: InputStream? = null
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        presenter = SignupPresenterImplementation(this, this)
        title = intent.getStringExtra(Constant.PROFILE_TYPE)


        init()
    }

    private fun init() {

        binding.edtPassword.transformationMethod = PasswordTransformationMethod()
        binding.edtComfirmPassword.transformationMethod = PasswordTransformationMethod()
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

        binding.imgUser1.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.USER)
        })

        binding.imgUser.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.USER)
        })

        binding.btnNext.setOnClickListener(View.OnClickListener {
            registerUser()
//            gotoDocumentActivity()
        })

        binding.tvSignin.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


    }

    private fun registerUser() {


        if (binding.edtName.text.toString().trim().isEmpty()) {

            showSuccessDialog(this, "Enter name")

        } else if (binding.edtMobile.text.toString().trim().isEmpty()) {

            showSuccessDialog(this, "Enter Mobile Number")
        } else if (binding.edtMobile.text.toString().trim().length != 10) {
            showSuccessDialog(this, "Invalid Mobile Number")

        } else if (binding.edtEmail.text.toString().trim().isEmpty()) {
            showSuccessDialog(this, "Enter Email")
        } else if (!Utility.emailValidator(binding.edtEmail.text.toString().trim())) {
            showSuccessDialog(this, "Invalid Email")
        } else if (binding.edtPassword.text.toString().isEmpty()) {
            showSuccessDialog(this, "Enter Password")

        } else if (!Utility.passwordValidator(binding.edtPassword.text.trim().toString())) {
            showSuccessDialog(this, "Password Must contain at least 1 number, 1 uppercase ,1 lowercase ,1 special(!@#\$%^&*?) character and at least 6 characters")

        } else if (binding.edtComfirmPassword.text.trim().toString().isEmpty()) {
            showSuccessDialog(this, "Enter Confirm Password")
        } else if (!binding.edtComfirmPassword.text.trim().toString().equals(binding.edtPassword.text.trim().toString())) {
            showSuccessDialog(this, "Passwords do not match")
        } else if (binding.edtResAddress.text.toString().trim().isEmpty()) {
            showSuccessDialog(this, "Enter Residential Address")
        } else if (binding.edtPostAddress.text.toString().trim().isEmpty()) {
            showSuccessDialog(this, "Enter Postal Address")
        } else if (file == null) {
            showSuccessDialog(this, "Select Image")
        } else {
            var json = JsonObject()

            json.addProperty("name", binding.edtName.text.toString().trim())
            json.addProperty("mobile", binding.edtMobile.text.toString().trim())
            json.addProperty("email", binding.edtEmail.text.toString().trim())
            json.addProperty("password", binding.edtPassword.text.trim().toString())
            json.addProperty(
                "residential_address",
                binding.edtResAddress.text.toString().trim()
            )
            json.addProperty(
                "postal_address",
                binding.edtPostAddress.text.toString().trim()
            )

            when (title) {
                Constant.OWNER ->
                    json.addProperty("account_type", "1")
                Constant.DRIVER ->
                    json.addProperty("account_type", "2")
            }

            json.addProperty(
                "device_type",
                "2"
            )
            json.addProperty(
                "device_token",
                Utility.getDeviceToken(this, Constant.D_TOKEN)
            )
            presenter!!.signupApi(json)
            Log.e("Input JsonObject", json.toString())
        }
//        gotoDocumentActivity()
    }


    override fun validateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onRegisterSuccess(responseData: RegisterResponse) {
//        Toast.makeText(this, responseData.message, Toast.LENGTH_LONG).show()
        Utility.setSharedPreference(getContext(), Constant.NAME, responseData.data.name)
        Utility.setSharedPreference(getContext(), Constant.MOBILE, responseData.data.mobile)
        Utility.setSharedPreference(getContext(), Constant.EMAIL, responseData.data.email)
        Utility.setSharedPreference(
            getContext(),
            Constant.RESIDENTIAL_ADDRESS,
            responseData.data.residentialAddress
        )
        Utility.setSharedPreference(
            getContext(),
            Constant.POSTAL_ADDRESS,
            responseData.data.postalAddress
        )
        Utility.setSharedPreference(getContext(), Constant.S_TOKEN, responseData.data.deviceToken)
        Utility.setSharedPreference(getContext(), Constant.USER_ID, responseData.data.userid)
        Utility.setSharedPreference(getContext(), Constant.API_KEY, responseData.data.stoken)

        if (responseData.data.accountType == 1) {
            Utility.setSharedPreference(getContext(), Constant.ACCOUNT_TYPE, Constant.OWNER)
        } else {
            Utility.setSharedPreference(getContext(), Constant.ACCOUNT_TYPE, Constant.PARTNER)
        }


        if (file != null) {
            presenter!!.uploadProfileImage(file!!)
        }

    }

    override fun onRegisterFailure(message: String) {

        DialogUtils.showSuccessDialog(this, message)
    }

    override fun getContext(): Context {
        return this
    }

    override fun onImageUploadSuccess(responseData: UploadImageResponse) {

        gotoDocumentActivity()

    }


    private fun gotoDocumentActivity() {

        when (title) {

            Constant.OWNER -> {
                val intent = Intent(this, OwnerDocActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, title)
                startActivity(intent)
            }

            Constant.DRIVER -> {
                val intent = Intent(this, DriverDocumentsActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, title)
                intent.putExtra("value", 1)
                startActivity(intent)
            }


        }


    }

    fun showAlertDialog(context: Context, msg: String) {

        try {

            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(msg)
                .setConfirmText("Ok")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                }
                .show()
        } catch (e: java.lang.Exception) {
            Log.e(javaClass.simpleName, "" + e.toString())

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun uploadDocument(type: String) {


        if (checkProfilePermissions()) {
            this.upload_type = type
            val singleSelectionPicker = BSImagePicker.Builder(Constant.PROVIDE_AUTHORITY)
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                //.hideCameraTile() //Default: show. Set this if you don't want user to take photo.
                .hideGalleryTile() //Default: show. Set this if you don't want to further let user select from a gallery app. In such case, I suggest you to set maximum     displaying    images to Integer.MAX_VALUE.
                .build()

            singleSelectionPicker.show(supportFragmentManager, "Select Image")

        } else {
            //Log.e("Permission ","Not Granted")

            requestPermission()
        }
    }

    private fun checkProfilePermissions(): Boolean {

        return (ActivityCompat.checkSelfPermission(
            this,
            profilePermissionsRequired[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            profilePermissionsRequired[1]
        ) == PackageManager.PERMISSION_GRANTED)


    }


    private fun requestPermission() {
        if (!checkProfilePermissions()) {
            Log.e("Permission ", "Not Granted")


            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    profilePermissionsRequired[0]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    profilePermissionsRequired[1]
                ))
            ) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request")
                builder.setMessage("This app needs storage and camera permission for captured and save image.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Constant.profilePermissionsRequired[0],Constant.profilePermissionsRequired[1]),
                        PROFILE_PERMISSION_CALLBACK
                    )
                }

                builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()
                    }
                })

                builder.show()
            } else if (
                Utility.getSharedPreferencesBoolean(
                    this,
                    profilePermissionsRequired[0]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request")
                builder.setMessage("This app needs storage and camera permission for captured and save image.")
                builder.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()
                        sentToSettings = true

                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", applicationContext.packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING)

                        Toast.makeText(
                            applicationContext,
                            "Go to Permissions to Grant Storage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

                builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()

                    }
                })

                builder.show()
            } else {
                //just request_old the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Constant.profilePermissionsRequired[0],Constant.profilePermissionsRequired[1]),
                    PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                this,
                profilePermissionsRequired[0],
                true
            )

        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PROFILE_PERMISSION_CALLBACK) {

            var allPermissionsGranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = true
                } else {
                    allPermissionsGranted = false
                    break
                }
            }


            if (allPermissionsGranted) {
                proceedAfterPermission()

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        profilePermissionsRequired[0]
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        profilePermissionsRequired[1]
                    )
                ) {

                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Permission request")
                    builder.setMessage("This app needs storage and camera permission for captured and save image.")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()


                        ActivityCompat.requestPermissions(
                            this,
                            profilePermissionsRequired,
                            PROFILE_PERMISSION_CALLBACK
                        )
                    }
                    builder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    //                    Toast.makeText(mActivity, "Unable to get required permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private fun proceedAfterPermission() {
        Toast.makeText(
            this,
            "We got required permissions for profile.",
            Toast.LENGTH_LONG
        ).show()
        //updateParentProfileImage();

    }


    override fun onSingleImageSelected(uri: Uri) {
        if (upload_type.equals(Constant.UploadType.USER, ignoreCase = true)) {
//            binding.imgUser.setScaleType(ImageView.ScaleType.FIT_XY)

            this.file = File(uri.path)
            binding.imgUser1.visibility = View.GONE
            binding.imgUser.visibility = View.VISIBLE
            try {
                Picasso.with(this).load(uri).into(binding.imgUser)
                inputStream = this.contentResolver.openInputStream(uri)
            } catch (e: java.lang.Exception) {

            }





            try {
                var licenceBackInputStream =
                    this.contentResolver.openInputStream(uri)
                Log.e("licenceBackInputStream", licenceBackInputStream.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }


}



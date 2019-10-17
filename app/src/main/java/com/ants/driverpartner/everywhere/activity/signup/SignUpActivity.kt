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
import com.ants.driverpartner.everywhere.activity.documents.DocumentActivity
import com.ants.driverpartner.everywhere.activity.partnerDocument.PartnerDocActivity
import com.ants.driverpartner.everywhere.activity.signup.model.RegisterResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivitySignupBinding
import com.ants.driverpartner.everywhere.utils.SnackbarUtils
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


        if (title.equals(Constant.BOTH)) {
            binding.tvTitle.text = Constant.OWNER + " Sign Up"
        } else {
            binding.tvTitle.text = title + " Sign Up"
        }
        init()
    }

    private fun init() {
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

        binding.imgUser1.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.USER)
        })

        binding.imgAddImage.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.USER)
        })

        binding.btnNext.setOnClickListener(View.OnClickListener {
            registerUser()
        })


    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.CAMERA
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun uploadDocument(type: String) {


        if (checkProfilePermissions()
        ) {
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
        if ((ActivityCompat.checkSelfPermission(
                applicationContext,
                profilePermissionsRequired[0]
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                applicationContext,
                profilePermissionsRequired[1]
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true

        } else {
            return false

        }

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

                val builder = AlertDialog.Builder(applicationContext)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs storage and camera permission for captured and save image.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        profilePermissionsRequired,
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
                    applicationContext,
                    profilePermissionsRequired[0]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(applicationContext)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs storage and camera permission for captured and save image.")
                builder.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()
                        sentToSettings = true

                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", applicationContext.getPackageName(), null)
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
                    profilePermissionsRequired,
                    PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                applicationContext,
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
                    builder.setTitle("Permission request_old")
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
            applicationContext,
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
            binding.imgAddImage.visibility = View.VISIBLE

            Picasso.with(applicationContext).load(uri).into(binding.imgUser)
            inputStream = applicationContext.getContentResolver().openInputStream(uri)


            try {
                var licenceBackInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("licenceBackInputStream", licenceBackInputStream.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }


    private fun registerUser() {

        if (binding.edtName.text.toString().trim().isEmpty()) {
            binding.edtName.setError("Enter name")
//            showAlertDialog(this, "Enter name")
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter name")

        } else if (binding.edtMobile.text.toString().trim().isEmpty()) {
            binding.edtMobile.setError("Enter Mobile Number")
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Mobile Number")
        } else if (binding.edtMobile.text.toString().trim().length != 10) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Invalid Mobile Number")
            binding.edtMobile.setError("Invalid Mobile Number")
        } else if (binding.edtEmail.text.toString().trim().isEmpty()) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Email")
            binding.edtMobile.setError("Enter Email")
        } else if (!Utility.emailValidator(binding.edtEmail.text.toString().trim())) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Invalid Email")
            binding.edtEmail.setError("Invalid Email")
        } else if (binding.edtPassword.text.toString().isEmpty()) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Password")
            binding.edtPassword.setError("Enter Password")
        } else if (binding.edtComfirmPassword.text.toString().isEmpty()) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Password")
            binding.edtPassword.setError("Enter Password")
        } else if (!binding.edtComfirmPassword.text.toString().equals(binding.edtPassword.text.toString())) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Password not match")
            binding.edtComfirmPassword.setError("Password not match")
        } else if (binding.edtResAddress.text.toString().trim().isEmpty()) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Residential Address")
            binding.edtResAddress.setError("Enter Residential Address")
        } else if (binding.edtPostAddress.text.toString().trim().isEmpty()) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Enter Postal Address")
            binding.edtPostAddress.setError("Enter Postal Address")
        } else if (file == null) {
            SnackbarUtils.snackBarBottom(binding.edtName, "Select Image")
            // binding.edtPostAddress.setError("Enter Postal Address")
        } else {
            var json = JsonObject()

            json.addProperty("name", binding.edtName.text.toString().trim())
            json.addProperty("mobile", binding.edtMobile.text.toString().trim())
            json.addProperty("email", binding.edtEmail.text.toString().trim())
            json.addProperty("password", binding.edtPassword.text.toString())
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
                    json.addProperty("account_type", 1)
                Constant.PARTNER ->
                    json.addProperty("account_type", 2)
                Constant.BOTH ->
                    json.addProperty("account_type", 3)

            }

            json.addProperty(
                "device_type",
                "1"
            )
            json.addProperty(
                "device_token",
                "adgasdgasg"
            )
            presenter!!.signupApi(json)
            Log.e("Input JsonObject", json.toString())
        }
//        gotoDocumentActivity()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onRegisterSuccess(responseData: RegisterResponse) {
        Toast.makeText(applicationContext, responseData.message, Toast.LENGTH_LONG).show()


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
        }else if(responseData.data.accountType == 2){
            Utility.setSharedPreference(getContext(), Constant.ACCOUNT_TYPE, Constant.PARTNER)
        }else {
            Utility.setSharedPreference(getContext(), Constant.ACCOUNT_TYPE, Constant.BOTH)
        }


        if (file != null) {
            presenter!!.uploadProfileImage(file!!)
        }

    }

    override fun getContext(): Context {
        return this
    }

    override fun onImageUploadSuccess(responseData: UploadImageResponse) {
        //Toast.makeText(applicationContext, responseData.message, Toast.LENGTH_LONG).show()

        gotoDocumentActivity()

    }

    private fun gotoDocumentActivity() {

        when (title) {

            Constant.OWNER -> {
                val intent = Intent(applicationContext, DocumentActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, title)
                startActivity(intent)
            }

            Constant.PARTNER -> {
                val intent = Intent(applicationContext, PartnerDocActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, title)
                intent.putExtra("value", 1)
                startActivity(intent)
            }

            Constant.BOTH -> {
                val intent = Intent(applicationContext, DocumentActivity::class.java)
                intent.putExtra(Constant.PROFILE_TYPE, title)
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
}



package com.ants.driverpartner.everywhere.activity.Signup

import android.Manifest
import android.app.AlertDialog
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
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.Constant.Companion.PROFILE_PERMISSION_CALLBACK
import com.ants.driverpartner.everywhere.Constant.Companion.REQUEST_PERMISSION_SETTING
import com.ants.driverpartner.everywhere.Constant.Companion.profilePermissionsRequired
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.documents.DocumentActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivitySignupBinding
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.tekzee.amiggos.ui.login.SignupPresenterImplementation


class SignUpActivity : BaseMainActivity(), SignupPresenter.SignupMainView,
    BSImagePicker.OnSingleImageSelectedListener {

    lateinit var binding: ActivitySignupBinding
    var title: String = ""
    private var sentToSettings = false
    private var upload_type: String? = null
    private var presenter: SignupPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        presenter = SignupPresenterImplementation(this, this)
        title = intent.getStringExtra(Constant.PROFILE_TYPE)
        binding.tvTitle.text = title + " Sign Up"

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

    private fun registerUser() {


        if (binding.edtName.text.toString().trim().isEmpty()) {
            binding.edtName.setError("Enter Name")
        } else if (binding.edtMobile.text.toString().trim().isEmpty()) {
            binding.edtMobile.setError("Enter Mobile Number")
        } else if (binding.edtMobile.text.toString().trim().length != 10) {
            binding.edtMobile.setError("Invalid Mobile Number")
        } else if (binding.edtEmail.text.toString().trim().isEmpty()) {
            binding.edtMobile.setError("Enter Email")
        } else if (!Utility.emailValidator(binding.edtEmail.text.toString().trim())) {
            binding.edtEmail.setError("Invalid Email")
        } else if (binding.edtPassword.text.toString().isEmpty()) {
            binding.edtPassword.setError("Enter Password")
        } else if (binding.edtComfirmPassword.text.toString().isEmpty()) {
            binding.edtPassword.setError("Enter Password")
        } else if (!binding.edtComfirmPassword.text.toString().equals(binding.edtPassword.text.toString())) {
            binding.edtComfirmPassword.setError("Password not match")
        } else if (binding.edtResAddress.text.toString().trim().isEmpty()) {
            binding.edtResAddress.setError("Enter Residential Address")
        } else if (binding.edtPostAddress.text.toString().trim().isEmpty()) {
            binding.edtPostAddress.setError("Enter Postal Address")
        } else {
            var json = JsonObject()

            json.addProperty("name", binding.edtName.text.toString().trim().isEmpty())
            json.addProperty("mobile", binding.edtMobile.text.toString().trim().isEmpty())
            json.addProperty("email", binding.edtEmail.text.toString().trim().isEmpty())
            json.addProperty("password", binding.edtPassword.text.toString().trim().isEmpty())
            json.addProperty(
                "residential_address",
                binding.edtResAddress.text.toString().trim().isEmpty()
            )
            json.addProperty(
                "postal_address",
                binding.edtPostAddress.text.toString().trim().isEmpty()
            )

            when (title) {
                Constant.OWNER ->
                    json.addProperty("account_type", 1)
                Constant.PARTNER ->
                    json.addProperty("account_type", 2)

                Constant.BOTH ->
                    json.addProperty("account_type", 3)

            }
            presenter!!.signupApi(json)

        }


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

            binding.imgUser1.visibility = View.GONE
            binding.imgUser.visibility = View.VISIBLE
            binding.imgAddImage.visibility = View.VISIBLE

            Picasso.with(applicationContext).load(uri).into(binding.imgUser)


            try {
                var licenceBackInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("licenceBackInputStream", licenceBackInputStream.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRegisterSuccess(responseData: RegisterResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}



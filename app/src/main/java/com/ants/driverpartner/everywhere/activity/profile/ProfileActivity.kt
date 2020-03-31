package com.ants.driverpartner.everywhere.activity.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.Constant.Companion.PROFILE_PERMISSION_CALLBACK
import com.ants.driverpartner.everywhere.Constant.Companion.REQUEST_PERMISSION_SETTING
import com.ants.driverpartner.everywhere.Constant.Companion.profilePermissionsRequired
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.profile.model.GetProfileResponse
import com.ants.driverpartner.everywhere.activity.profile.model.UpdateProfileResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityProfileBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import java.io.File
import java.io.InputStream

class ProfileActivity : BaseMainActivity(), ProfileView,
    BSImagePicker.OnSingleImageSelectedListener {


    lateinit var binding: ActivityProfileBinding
    private var presenter: ProfilePresenter? = null
    private var upload_type: String? = null
    private var sentToSettings = false
    private var file: File? = null
    private var inputStream: InputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        presenter = ProfilePresenter(this, this)
        initVeiw()
    }

    private fun initVeiw() {

        presenter!!.getProfile()



        binding.btnSave.setOnClickListener(View.OnClickListener {
            updateProfile()
        })


        binding.imageView.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.USER)
        })

        binding.imgBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }

    private fun updateProfile() {

        if (binding.edtName.text.trim().toString().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Name")
        } else if (binding.edtEmail.text.trim().toString().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Email")
        } else if (binding.edtMobile.text.trim().toString().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Mobile Number")
        } else {

            var jsonInput = JsonObject()
            jsonInput.addProperty("userid", Utility.getSharedPreferences(this, Constant.USER_ID))
            jsonInput.addProperty("name", binding.edtName.text.trim().toString())
            jsonInput.addProperty("mobile", binding.edtMobile.text.trim().toString())
            jsonInput.addProperty("email", binding.edtEmail.text.trim().toString())


//            presenter!!.updateProfile(jsonInput)

        }
    }

    override fun onGetProfile(data: List<GetProfileResponse.Data>) {

        if (data.size > 0) {

            binding.edtName.setText(data[0].name)
            binding.edtEmail.setText(data[0].email)
            binding.edtMobile.setText(data[0].mobile)
            binding.edtResidentialAddress.setText(data[0].residentialAddress)
            binding.edtPostalAddress.setText(data[0].postalAddress)


            Picasso.with(this).load(data[0].profileImage).into(binding.imgProfile)


        }


    }

//    override fun onUpdateProfile(message: String) {
//
//        DialogUtils.showAlertDialog(this, message)
//    }


    override fun getEmail(): String {
        return binding.edtEmail.text.trim().toString()
    }

    override fun getMobile(): String {
        return binding.edtMobile.text.trim().toString()
    }


    override fun getName(): String {
        return binding.edtName.text.trim().toString()
    }

    override fun getPostalAddress(): String {
        return binding.edtPostalAddress.text.toString()
    }

    override fun getResidentialAddress(): String {
        return binding.edtResidentialAddress.text.toString()
    }

    override fun onImageUploadSuccess(responseData: UploadImageResponse) {
        DialogUtils.showSuccessDialog(this, responseData.message)
    }

    override fun onUpdateProfile(responseData: UpdateProfileResponse) {
        DialogUtils.showSuccessDialog(this, responseData.message)
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

        return (ActivityCompat.checkSelfPermission(
            this.applicationContext,
            profilePermissionsRequired[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            applicationContext,
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

    override fun onSingleImageSelected(uri: Uri) {
        if (upload_type.equals(Constant.UploadType.USER, ignoreCase = true)) {
//            binding.imgUser.setScaleType(ImageView.ScaleType.FIT_XY)

            this.file = File(uri.path)

            Picasso.with(applicationContext).load(uri).into(binding.imgProfile)

            inputStream = applicationContext.getContentResolver().openInputStream(uri)

            if (file != null) {
                presenter!!.uploadProfileImage(file!!)
            }


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

        DialogUtils.showSuccessDialog(this, message)


    }


    override fun getContext(): Context {

        return this
    }
}

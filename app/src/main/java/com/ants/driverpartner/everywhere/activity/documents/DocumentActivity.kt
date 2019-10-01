package com.ants.driverpartner.everywhere.activity.documents

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.Constant.Companion.PROFILE_PERMISSION_CALLBACK
import com.ants.driverpartner.everywhere.Constant.Companion.REQUEST_PERMISSION_SETTING
import com.ants.driverpartner.everywhere.Constant.Companion.profilePermissionsRequired
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.base.MvpActivity
import com.ants.driverpartner.everywhere.activity.vehicleInfo.VehicleInfoActivity
import com.ants.driverpartner.everywhere.databinding.ActivityDocumentBinding
import com.ants.driverpartner.everywhere.uitl.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide

class DocumentActivity : MvpActivity<DocumentPresenter>(), DocumentView,
    BSImagePicker.OnSingleImageSelectedListener {
    private var sentToSettings = false
    lateinit var binding: ActivityDocumentBinding
    private var upload_type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document)

        requestPermission()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.btnUpload.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(applicationContext, VehicleInfoActivity::class.java)
            startActivity(intent)
        })

        binding.imgIdFront.setOnClickListener(View.OnClickListener { v ->
            uploadDocument(Constant.UploadType.ID_FRONT)
        })
        binding.imgIdBack.setOnClickListener(View.OnClickListener { v ->
            uploadDocument(Constant.UploadType.ID_BACK)
        })
        binding.imgLicenseFront.setOnClickListener(View.OnClickListener { v ->
            uploadDocument(Constant.UploadType.LICENCE_FRONT)
        })
        binding.imgLicenseBack.setOnClickListener(View.OnClickListener { v ->
            uploadDocument(Constant.UploadType.LICENCE_BACK)
        })

    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(
                    mActivity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mActivity,
                    Manifest.permission.CAMERA
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun uploadDocument(idFront: String) {


        if (checkProfilePermissions()
        ) {
            this.upload_type = idFront
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
                        mActivity,
                        profilePermissionsRequired[0]
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,
                        profilePermissionsRequired[1]
                    )
                ) {

                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(mActivity)
                    builder.setTitle("Permission request_old")
                    builder.setMessage("This app needs storage and camera permission for captured and save image.")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()


                        ActivityCompat.requestPermissions(
                            mActivity,
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

    override fun createPresenter(): DocumentPresenter {
        return DocumentPresenter(this)
    }


    override fun onSingleImageSelected(uri: Uri) {
        if (upload_type.equals(Constant.UploadType.ID_FRONT, ignoreCase = true)) {

            binding.imgIdFront.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(applicationContext).load(uri).into(binding.imgIdFront)

            try {
                var idFrontInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("idFrontInputStream", idFrontInputStream.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }

//            val imageBytes = Utility.getBytes(licenseInputStream)
//            mvpPresenter.uploadDocument(UploadType.LICENCE, imageBytes)

        } else if (upload_type.equals(Constant.UploadType.ID_BACK, ignoreCase = true)) {
            binding.imgIdBack.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(applicationContext).load(uri).into(binding.imgIdBack)

            try {
                var idBackInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("idBackInputStream", idBackInputStream.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

//            val imageBytes = Utility.getBytes(vehicleRegistrationInputStream)
//            mvpPresenter.uploadDocument(UploadType.VEHICLE_REGISTRATION, imageBytes)

        } else if (upload_type.equals(Constant.UploadType.LICENCE_FRONT, ignoreCase = true)) {
            binding.imgLicenseFront.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(applicationContext).load(uri).into(binding.imgLicenseFront)

            try {
                var licenceFrontInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("licenceFrontInputStream", licenceFrontInputStream.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }

//            val imageBytes = Utility.getBytes(insuranceInputStream)
//            mvpPresenter.uploadDocument(UploadType.INSURANCE, imageBytes)

        } else if (upload_type.equals(Constant.UploadType.LICENCE_BACK, ignoreCase = true)) {
            binding.imgLicenseBack.setScaleType(ImageView.ScaleType.FIT_XY)
            Glide.with(applicationContext).load(uri).into(binding.imgLicenseBack)

            try {
                var licenceBackInputStream =
                    applicationContext.getContentResolver().openInputStream(uri)
                Log.e("licenceBackInputStream", licenceBackInputStream.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }


//            val imageBytes = Utility.getBytes(idProofInputStream)
//            mvpPresenter.uploadDocument(UploadType.ID_PROOF, imageBytes)

        }


    }
}

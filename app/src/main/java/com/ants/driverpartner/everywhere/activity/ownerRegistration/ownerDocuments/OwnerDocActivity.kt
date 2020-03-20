package com.ants.driverpartner.everywhere.activity.ownerRegistration.ownerDocuments

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
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
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.Constant.Companion.PROFILE_PERMISSION_CALLBACK
import com.ants.driverpartner.everywhere.Constant.Companion.REQUEST_PERMISSION_SETTING
import com.ants.driverpartner.everywhere.Constant.Companion.profilePermissionsRequired
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.VehicleActivity
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityDocumentBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import java.io.File

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class OwnerDocActivity : BaseMainActivity(),
    OwnerDocPresenter.DocumentView,
    BSImagePicker.OnSingleImageSelectedListener {


    private var title = ""
    private var sentToSettings = false
    lateinit var binding: ActivityDocumentBinding
    private var upload_type: String? = null
    private var file_id_front: File? = null
    private var file_id_back: File? = null
    private var file_licence_front: File? = null
    private var file_licence_back: File? = null
    private var file_driver: File? = null
    private var file_home_address: File? = null
    private var file_bank_letter: File? = null
    private var file_bank_statement: File? = null
    private var file_ownership: File? = null
    private var presenter: OwnerDocPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document)
        presenter =
            OwnerDocPresenterImplementation(
                this,
                this
            )

        requestPermission()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.btnUpload.setOnClickListener(View.OnClickListener { v ->

            upload()

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

        binding.imgDriverPic.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.DRIVER_FACE)
        })

        binding.imgHomeAddress.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.HOME_ADDRESS)
        })

        binding.imgBankLatter.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.BANK_LATTER)
        })

        binding.imgBankStatement.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.BANK_STATEMENT)
        })

        binding.imgOwnership.setOnClickListener(View.OnClickListener {
            uploadDocument(Constant.UploadType.OWNERSHIP)
        })

    }

    private fun upload() {


        if (file_id_front == null) {

            DialogUtils.showSuccessDialog(this,"Please upload all documents")

        } else if (file_id_back == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_licence_front == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_licence_back == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_home_address == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_ownership == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_bank_letter == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else if (file_bank_statement == null) {
            DialogUtils.showSuccessDialog(this,"Please upload all documents")
        } else {
            val intent = Intent(applicationContext, VehicleActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (checkSelfPermission(
                    this,
                    WRITE_EXTERNAL_STORAGE
                ) === PERMISSION_GRANTED && checkSelfPermission(
                    this,
                    CAMERA
                ) === PERMISSION_GRANTED
            ) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun uploadDocument(uploadType: String) {


        if (checkProfilePermissions()
        ) {
            this.upload_type = uploadType
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
        return (checkSelfPermission(
            applicationContext,
            profilePermissionsRequired[0]
        ) == PERMISSION_GRANTED && checkSelfPermission(
            applicationContext,
            profilePermissionsRequired[1]
        ) == PERMISSION_GRANTED)

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
                if (grantResults[i] == PERMISSION_GRANTED) {
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

//    override fun createPresenter(): DocumentPresenter {
//        return DocumentPresenter(this)
//    }


    override fun onSingleImageSelected(uri: Uri) {

        when (upload_type) {
            Constant.UploadType.ID_FRONT -> {

                binding.imgIdFront.setScaleType(ImageView.ScaleType.FIT_XY)

                this.file_id_front = File(uri.path)

                Glide.with(applicationContext).load(uri).into(binding.imgIdFront)

                presenter!!.uploadDocument("idproof_front", file_id_front!!)
            }

            Constant.UploadType.ID_BACK -> {
                binding.imgIdBack.setScaleType(ImageView.ScaleType.FIT_XY)

                Glide.with(applicationContext).load(uri).into(binding.imgIdBack)

                this.file_id_back = File(uri.path)

                presenter!!.uploadDocument("idproof_back", file_id_back!!)
            }

            Constant.UploadType.LICENCE_FRONT -> {
                binding.imgLicenseFront.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgLicenseFront)

                this.file_licence_front = File(uri.path)

                presenter!!.uploadDocument("driver_license_front", file_licence_front!!)
            }

            Constant.UploadType.LICENCE_BACK -> {

                binding.imgLicenseBack.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgLicenseBack)

                this.file_licence_back = File(uri.path)

                presenter!!.uploadDocument("driver_license_back", file_licence_back!!)

            }
            Constant.UploadType.DRIVER_FACE -> {

                binding.imgDriverPic.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgDriverPic)

                this.file_driver = File(uri.path)

                presenter!!.uploadDocument("proffesional_driver_face", file_driver!!)

            }

            Constant.UploadType.HOME_ADDRESS -> {

                binding.imgHomeAddress.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgHomeAddress)

                this.file_home_address = File(uri.path)

                presenter!!.uploadDocument("proof_home_add", file_home_address!!)

            }


            Constant.UploadType.BANK_LATTER -> {

                binding.imgBankLatter.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgBankLatter)

                this.file_bank_letter = File(uri.path)

                presenter!!.uploadDocument("bank_letter", file_bank_letter!!)

            }
            Constant.UploadType.BANK_STATEMENT -> {

                binding.imgBankStatement.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgBankStatement)

                this.file_bank_statement = File(uri.path)

                presenter!!.uploadDocument("bank_statement", file_bank_statement!!)

            }
            Constant.UploadType.OWNERSHIP -> {

                binding.imgOwnership.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgOwnership)

                this.file_ownership = File(uri.path)

                presenter!!.uploadDocument(Constant.UploadType.OWNERSHIP, file_ownership!!)

            }
        }
    }


    override fun getContext(): Context {

        return this
    }

    override fun validateError(message: String) {

    }

    override fun onImageUploadSuccess(responseData: UploadImageResponse) {
        DialogUtils.showSuccessDialog(this, responseData.message)
    }
}

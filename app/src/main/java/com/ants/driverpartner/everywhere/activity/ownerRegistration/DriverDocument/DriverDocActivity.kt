package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverDetails.DriverListActivity
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.OwnersVehilce
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.model.RegisterDriverResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.VehicleAdaper
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.PartnerDocumentBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.selection_dialog.view.*
import java.io.File

class DriverDocActivity : BaseMainActivity(),
    DriverDocView, BSImagePicker.OnSingleImageSelectedListener,
    View.OnClickListener {


    lateinit var binding: PartnerDocumentBinding
    private var presenter: DriverDocPresenter? = null
    private var sentToSettings = false
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
    private var vehicleId = 0
    private var isAddDriver = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.partner_document)
        binding = DataBindingUtil.setContentView(this, R.layout.partner_document)
        presenter = DriverDocPresenter(this, this)
        isAddDriver = intent.getStringExtra(Constant.ADDING_DRIVER)

        init()
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

    private fun init() {

        if (isAddDriver.equals(Constant.ADDING_DRIVER)) {
            binding.back.visibility = View.VISIBLE
            binding.layoutSignin.visibility = View.GONE
        }

        binding.back.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        binding.imgIdFront.setOnClickListener(this)
        binding.imgIdBack.setOnClickListener(this)
        binding.imgLicenseFront.setOnClickListener(this)
        binding.imgLicenseBack.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.imgUser1.setOnClickListener(this)
        binding.imgHomeAddress.setOnClickListener(this)
        binding.imgBankLatter.setOnClickListener(this)
        binding.imgBankStatement.setOnClickListener(this)
        binding.imgOwnership.setOnClickListener(this)
        binding.edtVehicleType.setOnClickListener(this)
        binding.back.setOnClickListener(this)
    }

    override fun validateError(message: String) {


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_user1 -> uploadDocument(Constant.UploadType.DRIVER_FACE)
            R.id.img_user -> uploadDocument(Constant.UploadType.DRIVER_FACE)
            R.id.img_id_front -> uploadDocument(Constant.UploadType.ID_FRONT)
            R.id.img_id_back -> uploadDocument(Constant.UploadType.ID_BACK)
            R.id.img_license_front -> uploadDocument((Constant.UploadType.LICENCE_FRONT))
            R.id.img_license_back -> uploadDocument(Constant.UploadType.LICENCE_BACK)
            R.id.img_bank_latter -> uploadDocument(Constant.UploadType.BANK_LATTER)
            R.id.img_bank_statement -> uploadDocument(Constant.UploadType.BANK_STATEMENT)
            R.id.img_home_address -> uploadDocument(Constant.UploadType.HOME_ADDRESS)
            R.id.img_ownership -> uploadDocument(Constant.UploadType.OWNERSHIP)
            R.id.edt_vehicle_type -> presenter!!.getOwnerVehicel()
            R.id.btn_upload -> submit()
            R.id.back -> onBackPressed()
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun submit() {


        if (getName().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Please Enter Name")
        } else if (getEmail().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Enter Email")
        } else if (getMobileNumber().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Enter Mobile Number")
        } else if (getPassword().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Enter Password")
        } else if (getResidentialAddress().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Enter Residential Address")
        } else if (getPostalAddress().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Enter Postal Address")
        } else if (binding.edtVehicleType.text.toString().isEmpty()) {
            DialogUtils.showAlertDialog(this, "Please Select Vehicle Type")
        } else if (getIdProofFrontImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getIdProofBackImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getLicenceFrontImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getLicenceBackImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getHomeAddressImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getBankLatterImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else if (getBankStatementImage() == null) {
            DialogUtils.showAlertDialog(this, "Please Upload All Document")
        } else {
            presenter!!.registerDriver()

        }


    }

    override fun getName(): String {

        return binding.edtName.text.toString()
    }

    override fun getEmail(): String {
        return binding.edtEmail.text.toString()
    }

    override fun getMobileNumber(): String {
        return binding.edtMobile.text.toString()
    }

    override fun getPassword(): String {
        return binding.edtPassword.text.toString()
    }

    override fun getResidentialAddress(): String {
        return binding.edtResAdd.text.toString()
    }

    override fun getPostalAddress(): String {
        return binding.edtPostalAdd.text.toString()
    }

    override fun getVehicle(): String {
        return vehicleId.toString()
    }

    override fun getIdProofFrontImage(): File? {
        return file_id_front
    }

    override fun getIdProofBackImage(): File? {
        return file_id_back
    }

    override fun getLicenceFrontImage(): File? {
        return file_licence_front
    }

    override fun getLicenceBackImage(): File? {
        return file_licence_back
    }

    override fun getHomeAddressImage(): File? {
        return file_home_address
    }

    override fun getOwnershipImage(): File? {
        return file_ownership
    }

    override fun getBankLatterImage(): File? {
        return file_bank_letter
    }

    override fun getBankStatementImage(): File? {
        return file_bank_statement
    }

    override fun getDriverImage(): File? {
        return file_driver
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

    override fun onGetVehicleCategory(responseData: OwnersVehilce) {
        if (responseData.data.isNotEmpty()) {


            var dialogView = LayoutInflater.from(this).inflate(R.layout.selection_dialog, null)

            var dialog =
                android.app.AlertDialog.Builder(this).setView(dialogView).setCancelable(false)
                    .show()


            dialogView.tv_close.setOnClickListener(View.OnClickListener {

                dialog.dismiss()
            })

            dialogView.tv_header.text = "Select Vehicle Type"

            var data: ArrayList<VehicleCategory.Data>? = ArrayList()
            for (datas in responseData.data) {

                data!!.add(VehicleCategory.Data(datas.id, datas.registrationNumber))


            }


            var adaper = VehicleAdaper(this, data!!, object : VehicleAdaper.ItemClick {
                override fun onSelect(data: VehicleCategory.Data) {
                    dialog.dismiss()


                    binding.edtVehicleType.text = data.name
                    vehicleId = data.id


                    Utility.log(
                        javaClass.simpleName,
                        binding.edtVehicleType.text.toString() + "\t\t" + vehicleId
                    )

                }
            })

            dialogView.rv_category.hasFixedSize()
            dialogView.rv_category.layoutManager = LinearLayoutManager(this)

            dialogView.rv_category.adapter = adaper

        }
    }

    override fun onRegisterSuccess(responseData: RegisterDriverResponse) {
        DialogUtils.showCustomAlertDialog(
            this,
            responseData.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {

                    if (isAddDriver.equals(Constant.ADDING_DRIVER))

                        startDriverListActivity()
                    else
                        startLoginActivity()
                }
            })

    }

    private fun startDriverListActivity() {

        val intent = Intent(this, DriverListActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun startLoginActivity() {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onSingleImageSelected(uri: Uri) {

        when (upload_type) {
            Constant.UploadType.ID_FRONT -> {

                binding.imgIdFront.setScaleType(ImageView.ScaleType.FIT_XY)

                this.file_id_front = File(uri.path)

                Glide.with(applicationContext).load(uri).into(binding.imgIdFront)

                // presenter!!.uploadDocument("idproof_front", file_id_front!!)
            }

            Constant.UploadType.ID_BACK -> {
                binding.imgIdBack.setScaleType(ImageView.ScaleType.FIT_XY)

                Glide.with(applicationContext).load(uri).into(binding.imgIdBack)

                this.file_id_back = File(uri.path)

                //  presenter!!.uploadDocument("idproof_back", file_id_back!!)
            }

            Constant.UploadType.LICENCE_FRONT -> {
                binding.imgLicenseFront.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgLicenseFront)

                this.file_licence_front = File(uri.path)

                // presenter!!.uploadDocument("driver_license_front", file_licence_front!!)
            }

            Constant.UploadType.LICENCE_BACK -> {

                binding.imgLicenseBack.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgLicenseBack)

                this.file_licence_back = File(uri.path)

                // presenter!!.uploadDocument("driver_license_back", file_licence_back!!)

            }
            Constant.UploadType.DRIVER_FACE -> {


                binding.imgUser.visibility = View.VISIBLE
                binding.imgUser1.visibility = View.GONE
//                binding.imgAddImage.visibility = View.VISIBLE

                Picasso.with(applicationContext).load(uri).into(binding.imgUser)
//                inputStream = applicationContext.getContentResolver().openInputStream(uri)

//               /**/ binding.imgUser.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

//                Glide.with(applicationContext).load(uri).into(binding.imgUser)

                this.file_driver = File(uri.path)

                // presenter!!.uploadDocument("proffesional_driver_face", file_driver!!)

            }

            Constant.UploadType.HOME_ADDRESS -> {

                binding.imgHomeAddress.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgHomeAddress)

                this.file_home_address = File(uri.path)

                //presenter!!.uploadDocument("proof_home_add", file_home_address!!)

            }


            Constant.UploadType.BANK_LATTER -> {

                binding.imgBankLatter.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgBankLatter)

                this.file_bank_letter = File(uri.path)

                //  presenter!!.uploadDocument("bank_letter", file_bank_letter!!)

            }
            Constant.UploadType.BANK_STATEMENT -> {

                binding.imgBankStatement.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgBankStatement)

                this.file_bank_statement = File(uri.path)

                //  presenter!!.uploadDocument("bank_statement", file_bank_statement!!)

            }
            Constant.UploadType.OWNERSHIP -> {

                binding.imgOwnership.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(applicationContext).load(uri).into(binding.imgOwnership)

                this.file_ownership = File(uri.path)

                //  presenter!!.uploadDocument("bank_statement", file_bank_statement!!)

            }

        }
    }


    private fun checkProfilePermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            applicationContext,
            Constant.profilePermissionsRequired[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            applicationContext,
            Constant.profilePermissionsRequired[1]
        ) == PackageManager.PERMISSION_GRANTED)

    }

    private fun requestPermission() {
        if (!checkProfilePermissions()) {
            Log.e("Permission ", "Not Granted")


            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[0]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[1]
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
                        Constant.profilePermissionsRequired,
                        Constant.PROFILE_PERMISSION_CALLBACK
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
                    Constant.profilePermissionsRequired[0]
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
                        startActivityForResult(intent, Constant.REQUEST_PERMISSION_SETTING)

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
                    Constant.profilePermissionsRequired,
                    Constant.PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                applicationContext,
                Constant.profilePermissionsRequired[0],
                true
            )

        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PROFILE_PERMISSION_CALLBACK) {

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
                        Constant.profilePermissionsRequired[0]
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Constant.profilePermissionsRequired[1]
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
                            Constant.profilePermissionsRequired,
                            Constant.PROFILE_PERMISSION_CALLBACK
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

}

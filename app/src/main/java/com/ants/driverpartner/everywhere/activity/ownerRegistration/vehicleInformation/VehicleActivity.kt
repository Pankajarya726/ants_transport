package com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation

import android.app.DatePickerDialog
import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverDetails.DriverListActivity
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.DriverDocActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.ownerDocuments.OwnerDocActivity
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.RegisterVehicleResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.model.VehicleCategory
import com.ants.driverpartner.everywhere.activity.vehicleDetails.VehilcleListActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityVehicleBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.selection_dialog.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VehicleActivity : BaseMainActivity(), View.OnClickListener,
    BSImagePicker.OnSingleImageSelectedListener, VehicleView {


    lateinit var binding: ActivityVehicleBinding
    private var sentToSettings = false
    private var upload_type: String? = null
    private var file_img_license: File? = null
    private var filr_img_odometer: File? = null
    private var file_img_insurance: File? = null
    private var file_img_vehicle_front: File? = null
    private var file_img_vehicle_back: File? = null
    private var file_img_vehicle_right: File? = null
    private var file_img_vehicle_left: File? = null
    private var presenter: VehiclePresenter? = null
    private var cal = Calendar.getInstance()
    private var vehicleId = 0
    private var isAddVehicle = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle)

        presenter = VehiclePresenter(this, this)

        isAddVehicle = intent.getStringExtra(Constant.ADDING_VEHICLE)
        title = intent.getStringExtra(Constant.PROFILE_TYPE)

        init()


    }


    fun init() {

        if (isAddVehicle.equals(Constant.ADDING_VEHICLE)) {
            binding.back.visibility = View.VISIBLE
            binding.layoutSignin.visibility = View.GONE
        }

        binding.tvSignup.setOnClickListener(this)
        binding.imgLicensePic.setOnClickListener(this)
        binding.imgOdometer.setOnClickListener(this)
        binding.imgInsurence.setOnClickListener(this)
        binding.imgVehicleRight.setOnClickListener(this)
        binding.imgVehicleLeft.setOnClickListener(this)
        binding.imgVehicleFront.setOnClickListener(this)
        binding.imgVehicleBack.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        binding.edtRegDate.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        binding.tvVehicleType.setOnClickListener(this)
        binding.tvType.setOnClickListener(this)
        binding.back.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.tv_vehicle_type -> presenter!!.getVechicleCategory()

            R.id.img_license_pic -> uploadDocument(Constant.UploadType.VEHICLE_LICENSE)

            R.id.img_odometer -> uploadDocument(Constant.UploadType.VEHICLE_ODOMETER)

            R.id.img_insurence -> uploadDocument(Constant.UploadType.VEHICLE_INSURANCE)

            R.id.img_vehicle_front -> uploadDocument(Constant.UploadType.VEHICLE_FRONT)

            R.id.img_vehicle_back -> uploadDocument(Constant.UploadType.VEHICLE_BACK)

            R.id.img_vehicle_left -> uploadDocument(Constant.UploadType.VEHICLE_LEFT)

            R.id.img_vehicle_right -> uploadDocument(Constant.UploadType.VEHICLE_RIGHT)

            R.id.edt_reg_date -> {

                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)



                        updateDateInView()
                    }

                DatePickerDialog(
                    this,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            R.id.tv_signup -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }


            R.id.btn_upload -> submit()

            R.id.back -> {
                if (isAddVehicle.equals(Constant.ADDING_VEHICLE)) {
                    val intent = Intent(this, VehilcleListActivity::class.java)
                    startActivity(intent)
                    this.finish()
                } else {
                    onBackPressed()
                }
            }

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }

    private fun submit() {

        if (binding.tvVehicleType.text.trim().isEmpty()) {
            DialogUtils.showSuccessDialog(this, "Select Vehicle Type")
        } else
            if (binding.edtRegDate.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Select Registration Date")
            } else if (binding.edtTire.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Tare")
            } else if (binding.edtMass.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Gross Vehicle Mass")
            } else if (binding.edtRegNumber.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Vehicle  Registration Number")
            } else if (binding.edtModel.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Vehicle  Model")
            } else if (binding.edtManufacture.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Vehicle  Manufacture")
            } else if (binding.edtVin.text.trim().isEmpty()) {
                DialogUtils.showSuccessDialog(this, "Enter Vehicle  VIN Number")
            } else if (file_img_license == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle license")
            } else if (filr_img_odometer == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle odometer")
            } else if (file_img_insurance == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle insurance")
            } else if (file_img_vehicle_front == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle front side")
            } else if (file_img_vehicle_back == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle back side")
            } else if (file_img_vehicle_left == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle left side")
            } else if (file_img_vehicle_right == null) {
                DialogUtils.showSuccessDialog(this, "Select picture of vehicle right side")
            } else {

                presenter!!.callRegisterVehicleApi()


            }
    }


    override fun getVehicleType(): String {
        return vehicleId.toString()

    }

    override fun getRegistrationDate(): String {
        return binding.edtRegDate.text.toString()
    }

    override fun getTare(): String {
        return binding.edtTire.text.toString()
    }

    override fun getVehicleMass(): String {
        return binding.edtMass.text.toString()
    }

    override fun getRegistrationNumber(): String {
        return binding.edtRegNumber.text.toString()
    }

    override fun getModel(): String {
        return binding.edtModel.text.toString()
    }

    override fun getManufacture(): String {
        return binding.edtManufacture.text.toString()
    }

    override fun getLicenceImage(): File? {


        return file_img_license!!
    }

    override fun getOdometerImage(): File? {

        return filr_img_odometer!!

    }

    override fun getInsuranceImage(): File? {
        return file_img_insurance!!

    }

    override fun getVehicleFontImage(): File? {
        return file_img_vehicle_front!!

    }

    override fun getVehicelBackImage(): File? {
        return file_img_vehicle_back!!

    }

    override fun getVehicleLeftImage(): File? {
        return file_img_vehicle_right!!

    }

    override fun getVehicleRightImage(): File? {
        return file_img_vehicle_left!!
    }

    override fun getVinNumber(): String {
        return binding.edtVin.text.toString()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)


        binding.edtRegDate!!.text = sdf.format(cal.getTime())
    }


    override fun getContext(): Context {
        return this
    }

    override fun onGetVehicleCategory(responseData: VehicleCategory) {

        if (responseData.data.isNotEmpty()) {

            var dialogView = LayoutInflater.from(this).inflate(R.layout.selection_dialog, null)

            var dialog =
                android.app.AlertDialog.Builder(this).setView(dialogView).setCancelable(false)
                    .show()


            dialogView.tv_close.setOnClickListener(View.OnClickListener {

                dialog.dismiss()
            })

            dialogView.tv_header.text = "Select Vehicle Type"

            var adaper = VehicleAdaper(this, responseData.data, object : VehicleAdaper.ItemClick {
                override fun onSelect(data: VehicleCategory.Data) {
                    dialog.dismiss()


                    binding.tvVehicleType.text = data.name
                    vehicleId = data.id


                    Utility.log(
                        javaClass.simpleName,
                        binding.tvVehicleType.text.toString() + "\t\t" + vehicleId
                    )

                }
            })

            dialogView.rv_category.hasFixedSize()
            dialogView.rv_category.layoutManager = LinearLayoutManager(this)

            dialogView.rv_category.adapter = adaper

        }


    }

    override fun onRegisterSuccess(responseData: RegisterVehicleResponse) {

        DialogUtils.showCustomAlertDialog(
            this,
            responseData.message,
            object : DialogUtils.CustomDialogClick {

                override fun onOkClick() {

                    gotoNextActivity()
                }
            })


    }

    fun gotoNextActivity() {
        if (isAddVehicle.equals(Constant.ADDING_VEHICLE)) {
            val intent = Intent(this, VehilcleListActivity::class.java)
            startActivity(intent)
            this.finish()
        } else {


            if (title.equals(Constant.DRIVER)) {
                val intent = Intent(this, OwnerDocActivity::class.java)
                intent.putExtra(Constant.ADDING_DRIVER, "")
                intent.putExtra(Constant.PROFILE_TYPE, title)
                startActivity(intent)
                this.finish()
            } else {
                val intent = Intent(this, DriverDocActivity::class.java)
                intent.putExtra(Constant.ADDING_DRIVER, "")
                startActivity(intent)
                this.finish()
            }


        }
    }

    override fun validateError(message: String) {


        DialogUtils.showSuccessDialog(this, message)
    }

    private fun uploadDocument(idFront: String) {


        if (Utility.checkProfilePermissions(this)
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

    private fun requestPermission() {
        if (!Utility.checkProfilePermissions(this)) {
            Log.e("Permission ", "Not Granted")


            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[0]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[1]
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
                    this,
                    Constant.profilePermissionsRequired[0]
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
                this,
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
                    builder.setTitle("Permission request")
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
            this,
            "We got required permissions for profile.",
            Toast.LENGTH_LONG
        ).show()
        //updateParentProfileImage();

    }

    override fun onSingleImageSelected(uri: Uri?) {

        when (upload_type) {
            Constant.UploadType.VEHICLE_LICENSE -> {

                binding.imgLicensePic.scaleType = ImageView.ScaleType.FIT_XY

                if (uri != null) {
                    this.file_img_license = File(uri.path)
                }

                Glide.with(this).load(uri).into(binding.imgLicensePic)

                //  presenter!!.uploadDocument(Constant.UploadType.ID_FRONT, file_img_license!!)

            }

            Constant.UploadType.VEHICLE_ODOMETER -> {

                binding.imgOdometer.scaleType = ImageView.ScaleType.FIT_XY

                Glide.with(this).load(uri).into(binding.imgOdometer)

                this.filr_img_odometer = File(uri?.path)

                // presenter!!.uploadDocument(Constant.UploadType.ID_BACK, filr_img_odometer!!)

            }

            Constant.UploadType.VEHICLE_INSURANCE -> {

                binding.imgInsurence.scaleType = ImageView.ScaleType.FIT_XY

                Glide.with(this).load(uri).into(binding.imgInsurence)

                this.file_img_insurance = File(uri?.path)

                // presenter!!.uploadDocument(Constant.UploadType.LICENCE_FRONT, file_img_insurance!!)

            }

            Constant.UploadType.VEHICLE_FRONT -> {

                binding.imgVehicleFront.scaleType = ImageView.ScaleType.FIT_XY

                Glide.with(this).load(uri).into(binding.imgVehicleFront)

                this.file_img_vehicle_front = File(uri?.path)

                //  presenter!!.uploadDocument(Constant.UploadType.LICENCE_BACK, file_img_vehicle_front!!)

            }

            Constant.UploadType.VEHICLE_BACK -> {

                binding.imgVehicleBack.scaleType = ImageView.ScaleType.FIT_XY

                Glide.with(this).load(uri).into(binding.imgVehicleBack)

                this.file_img_vehicle_back = File(uri?.path)

                // presenter!!.uploadDocument(Constant.UploadType.DRIVER_FACE, file_img_vehicle_back!!)

            }

            Constant.UploadType.VEHICLE_LEFT -> {

                binding.imgVehicleLeft.setScaleType(ImageView.ScaleType.FIT_XY)

                Glide.with(this).load(uri).into(binding.imgVehicleLeft)

                this.file_img_vehicle_left = File(uri?.path)

                //   presenter!!.uploadDocument(Constant.UploadType.HOME_ADDRESS, file_img_vehicle_left!!)

            }

            Constant.UploadType.VEHICLE_RIGHT -> {

                binding.imgVehicleRight.setScaleType(ImageView.ScaleType.FIT_XY)

                Glide.with(this).load(uri).into(binding.imgVehicleRight)

                this.file_img_vehicle_right = File(uri?.path)

                //  presenter!!.uploadDocument(Constant.UploadType.BANK_LATTER, file_img_vehicle_right!!)

            }
        }
    }


//    override fun createPresenter(): DocumentPresenter {
//        return DocumentPresenter(this)
//   }
}

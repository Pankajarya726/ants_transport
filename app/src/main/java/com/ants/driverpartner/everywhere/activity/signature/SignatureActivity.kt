package com.ants.driverpartner.everywhere.activity.signature

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivitySignatureBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SignatureActivity : BaseMainActivity(), SignatureView {

    lateinit var binding: ActivitySignatureBinding
    private var btnClear: Button? = null
    var btnSave: Button? = null
    private var file: File? = null
    private var canvasLL: LinearLayout? = null
    private var view: LinearLayout? = null
    private var mSignature: signature? = null
    private var bitmap: Bitmap? = null
    // Creating Separate Directory for saving Generated Images
//    var DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/"
//    var pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    var StoredPath = DIRECTORY + pic_name + ".png"

//    var DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    var DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    var pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date());
    var StoredPath = DIRECTORY + pic_name + ".png";


    private var presenter: SignaturePresenter? = null
    private var sentToSettings = false

    private var bookingId = 0

//    var timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//    var imageFileName = "JPEG_" + timeStamp + "_"
//    var storageDir: File? = null
//    var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signature)
        presenter = SignaturePresenter(this, this)


        checkStoragePermission()
//        createFile()

        bookingId = intent.getIntExtra(Constant.BOOKING_ID, 0)

        mSignature = signature(this, null)
        mSignature!!.setBackgroundColor(Color.WHITE)
        // Dynamically generating Layout through java code
        canvasLL = binding.canvasLL

        canvasLL!!.addView(
            mSignature,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view = binding.canvasLL


        binding.btnSave.setOnClickListener(View.OnClickListener {
            view!!.isDrawingCacheEnabled = true

            mSignature!!.save(view!!, StoredPath)

            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT)
                .show()


            try {


                var imgfile = File(StoredPath)

                presenter!!.uploadSignature(imgfile!!, bookingId.toString())

            } catch (e: java.lang.Exception) {

                Log.e(javaClass.simpleName, e.localizedMessage)
            }
        })

        // Method to create Directory, if the Directory doesn't exists

        file = File(getExternalFilesDir("Signature"), "user");

        StoredPath = file!!.path + pic_name +".png"


        if (!file!!.exists()) {
            Log.e(javaClass.simpleName, "File Not exist")
            file!!.mkdir()
            Log.e(javaClass.simpleName, file!!.exists().toString())

        }

    }

    /* fun createFile() {
         file = File(DIRECTORY)

         if (!file!!.exists()) {
             Log.e(javaClass.simpleName, "File Not exist")
             file!!.mkdir()
             Log.e(javaClass.simpleName, file!!.exists().toString())

         }


 //        try {
 //            storageDir = File(Environment.getExternalStorageDirectory().toString(), "Signature/")
 //
 //            if (!storageDir!!.exists()) {
 //                storageDir!!.mkdirs()
 //            }
 //
 //             imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
 //            Log.e("our file", imageFile!!.getAbsolutePath())
 //        } catch (e: Exception) {
 //            Log.e("our file", e.localizedMessage)
 //        }


     }*/

    inner class signature(context: Context, attrs: AttributeSet?) : View(context, attrs) {
        private val paint = Paint()
        private val path = Path()
        private var lastTouchX: Float = 0.toFloat()
        private var lastTouchY: Float = 0.toFloat()
        private val dirtyRect = RectF()

        init {
            paint.setAntiAlias(true)
            paint.setColor(Color.BLACK)
            paint.setStyle(Paint.Style.STROKE)
            paint.setStrokeJoin(Paint.Join.ROUND)
            paint.setStrokeWidth(STROKE_WIDTH)
        }

        @SuppressLint("WrongThread")
        fun save(v: View, StoredPath: String) {

            Log.e(javaClass.simpleName, "StoredPath: " + StoredPath)
            Log.e(javaClass.simpleName, "Width: " + canvasLL!!.getWidth())
            Log.e(javaClass.simpleName, "Height: " + canvasLL!!.getHeight())


            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(
                    canvasLL!!.getWidth(),
                    canvasLL!!.getHeight(),
                    Bitmap.Config.RGB_565
                )
            }
            val canvas = Canvas(bitmap!!)
            try {


                // Output the file
                val mFileOutStream = FileOutputStream(StoredPath)
                v.draw(canvas)
                // Convert the output file to Image such as .png
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream)
                mFileOutStream.flush()
                mFileOutStream.close()
            } catch (e: Exception) {
                Log.e("log_tag", e.toString())
            }
            Log.e(javaClass.simpleName, "StoredPath: " + StoredPath)
        }

        fun clear() {
            path.reset()
            invalidate()

        }

        protected override fun onDraw(canvas: Canvas) {
            canvas.drawPath(path, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val eventX = event.x
            val eventY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(eventX, eventY)
                    lastTouchX = eventX
                    lastTouchY = eventY
                    return true
                }

                MotionEvent.ACTION_MOVE,

                MotionEvent.ACTION_UP -> {

                    resetDirtyRect(eventX, eventY)
                    val historySize = event.historySize
                    for (i in 0 until historySize) {
                        val historicalX = event.getHistoricalX(i)
                        val historicalY = event.getHistoricalY(i)
                        expandDirtyRect(historicalX, historicalY)
                        path.lineTo(historicalX, historicalY)
                    }
                    path.lineTo(eventX, eventY)
                }

                else -> {
                    debug("Ignored touch event: $event")
                    return false
                }
            }

            invalidate(
                (dirtyRect.left - HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.top - HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.right + HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.bottom + HALF_STROKE_WIDTH).toInt()
            )

            lastTouchX = eventX
            lastTouchY = eventY

            return true
        }

        private fun debug(string: String) {

            Log.v("log_tag", string)

        }

        private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY
            }
        }

        private fun resetDirtyRect(eventX: Float, eventY: Float) {
            dirtyRect.left = Math.min(lastTouchX, eventX)
            dirtyRect.right = Math.max(lastTouchX, eventX)
            dirtyRect.top = Math.min(lastTouchY, eventY)
            dirtyRect.bottom = Math.max(lastTouchY, eventY)
        }


    }

    companion object {

        private val STROKE_WIDTH = 5f
        private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2
    }

    override fun onSignatureUploadSuccess(responseData: UploadSignatureResponse) {
        Log.e(javaClass.simpleName, responseData.message)
//        gotoHomeActivity(responseData.message)

        DialogUtils.showCustomAlertDialog(
            this,
            responseData.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {
                    Log.e(javaClass.simpleName, "onOkClick" + responseData.message)

                    gotoHomeActivity(responseData.message)

                }
            })
    }

    override fun validateError(message: String) {
        Log.e(javaClass.simpleName, message)
    }

    fun gotoHomeActivity(message: String) {
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)
//        this.finish()

        Log.e(javaClass.simpleName, "gotoHomeActivity : $message")

        var data = Intent()
        var text = "$message"
        data.setData(Uri.parse(text))
        setResult(RESULT_OK, data)
        finish()
    }


    fun checkStoragePermission() {


        if (checkPermissions()) {
            Log.e(javaClass.simpleName, "Premission Granted")
//            createFile()

        } else {
            Log.e(javaClass.simpleName, "Premission Not Granted")
            requestPermission()
        }

    }

    private fun checkPermissions(): Boolean {

        return (ActivityCompat.checkSelfPermission(
            this,
            Constant.profilePermissionsRequired[1]
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        if (!checkPermissions()) {
            Log.e("Permission ", "Not Granted")


            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[1]
                )
            ) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs storage permission for save image.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Constant.profilePermissionsRequired[1]),
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
                    Constant.profilePermissionsRequired[1]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs storage permission for save image.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialogInterface, i ->
                    dialogInterface.cancel()
                    sentToSettings = true

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri =
                        Uri.fromParts("package", applicationContext.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, Constant.REQUEST_PERMISSION_SETTING)

                    Toast.makeText(
                        applicationContext,
                        "Go to Permissions to Grant Storage",
                        Toast.LENGTH_LONG
                    ).show()
                }

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
                    arrayOf(Constant.profilePermissionsRequired[1]),
                    Constant.PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                this,
                Constant.profilePermissionsRequired[1],
                true
            )

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission()


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PROFILE_PERMISSION_CALLBACK) {

            var allPermissionsGranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = true
//                    proceedAfterPermission()
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
                        Constant.profilePermissionsRequired[1]
                    )
                ) {

                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Permission request_old")
                    builder.setMessage("This app needs storage permission for save image.")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()


                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Constant.profilePermissionsRequired[1]),
                            Constant.PROFILE_PERMISSION_CALLBACK
                        )
                    }
                    builder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    // Toast.makeText(mActivity, "Unable to get required permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    fun proceedAfterPermission() {

//        createFile();
//        file = File(DIRECTORY)
//        if (!file!!.exists()) {
//            Log.e(javaClass.simpleName, "Not exist")
//            file!!.mkdirs()
//
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}

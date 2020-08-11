package com.ants.driverpartner.everywhere.activity.NavigationActivity

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.packageDetailActivity.PackageDetailActivity
import com.ants.driverpartner.everywhere.activity.signature.SignatureActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityCurrentBinding
import com.ants.driverpartner.everywhere.fragment.currentBooking.BottomSheetFragment
import com.ants.driverpartner.everywhere.fragment.currentBooking.CustomInterface
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject

class CurrentActivity : BaseMainActivity(), NavigationView, OnMapReadyCallback,
    com.google.android.gms.location.LocationListener,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    DirectionCallback, CustomInterface {


    lateinit var binding: ActivityCurrentBinding
    private var presenter: NavigationPresenter? = null
    private var first_time: Boolean = false
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var sentToSettings = false
    private var bluePolyline: Polyline? = null
    private var lightBluePolyline: Polyline? = null
    private var pickupLatLng = LatLng(28.61, 77.22)
    private var dropLatLng = LatLng(28.61, 76.22)
    private var mapFragment: SupportMapFragment? = null
    private var driverLatLong = LatLng(0.0, 0.0)
    private var previousLatLong: LatLng? = null
    private var pickUpMarker: Marker? = null
    private var dropMarker: Marker? = null
    private var driverMaker: Marker? = null
    private var pickMarkerOption: MarkerOptions? = null
    private var dropMarkerOption: MarkerOptions? = null
    private var driverMakerOption: MarkerOptions? = null
    var bottomSheetDialogFragment: BottomSheetDialogFragment? = null
    var getData: GetCurrentBookingRespone.Data? = null
    private var bookingId = 0
    private var arrivedAtCollection = false
    private val SMALLEST_DISPLACEMENT: Long = 5
    private var mLocationRequest: LocationRequest? = null
    val PERMISSION_REQUEST_CODE = 200
    val REQUEST_CHECK_SETTINGS = 101
    private val MAP_ZOOM_LEVEL = 16.5f
    private var bearing = 0.0f
    private var isFirstTime = true
    private var updateLocation = false

    private val INTERVAL = (1000 * 10).toLong()
    private val FASTEST_INTERVAL = (1000 / 2).toLong()
    private var bookingStatus = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current)
        presenter = NavigationPresenter(this, this)
//        view.setHeaderTitle(getString(R.string.current_fragment))


        init()
    }

    fun init() {

        chekLocationPermission()
        binding.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        binding.tvBookingInfo.setOnClickListener(View.OnClickListener {

            if (getData != null) {
                bottomSheetDialogFragment =
                    BottomSheetFragment(this, getData!!, bookingStatus)
                bottomSheetDialogFragment!!.show(supportFragmentManager, "Bottomsheet")
            }

        })

//        presenter!!.getCurrentBooking()
    }

    override fun onGetCurrentBooking(responseData: GetCurrentBookingRespone) {


        Log.e(javaClass.simpleName, responseData.data[0].packageDetail.senderLat)
        Log.e(javaClass.simpleName, responseData.data[0].packageDetail.senderLong)
        Log.e(javaClass.simpleName, responseData.data[0].packageDetail.receiverLat)
        Log.e(javaClass.simpleName, responseData.data[0].packageDetail.receiverLong)

        bookingId = responseData.data[0].bookingId

        getData = responseData.data[0]

        bookingStatus = responseData.data[0].bookingStatus

        if (bookingStatus == 2) {

            if (driverLatLong != LatLng(0.0, 0.0)) {


                updatePickUpLatiLong(driverLatLong.latitude, driverLatLong.longitude)
                updateDropLatiLong(
                    responseData.data[0].packageDetail.senderLat.toDouble(),
                    responseData.data[0].packageDetail.senderLong.toDouble()
                )

            } else {
                updatePickUpLatiLong(driverLatLong.latitude, driverLatLong.longitude)
                updateDropLatiLong(
                    responseData.data[0].packageDetail.senderLat.toDouble(),
                    responseData.data[0].packageDetail.senderLong.toDouble()
                )
            }


        } else {

            updatePickUpLatiLong(
                responseData.data[0].packageDetail.senderLat.toDouble(),
                responseData.data[0].packageDetail.senderLong.toDouble()
            )

            updateDropLatiLong(
                responseData.data[0].packageDetail.receiverLat.toDouble(),
                responseData.data[0].packageDetail.receiverLong.toDouble()
            )
        }


        pickMarkerOption = MarkerOptions().position(pickupLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pickup))

        dropMarkerOption = MarkerOptions().position(dropLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_drop))


        driverMakerOption = MarkerOptions().position(driverLatLong)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.nav_arrow)).title("You")

        pickUpMarker = mMap!!.addMarker(pickMarkerOption)
        dropMarker = mMap!!.addMarker(dropMarkerOption)

        mMap!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                pickupLatLng,
                Constant.MAP_ZOOM_LEVEL
            )
        )

        drawRouteOnMap(pickupLatLng, dropLatLng)
        updateLocation = true


        binding.btnNavigation.setOnClickListener(View.OnClickListener {
            if (dropLatLng != null) {
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=" + dropLatLng.latitude + "," + dropLatLng.longitude)
                var intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                //                    mapIntent.setPackage("com.google.android.apps.maps");
                if (!canHandleIntent(this, intent)) {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://detailsid=com.google.android.apps.maps")
                }

                startActivity(intent)

            } else {
                Toast.makeText(this, "Drop location not available", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun canHandleIntent(appContext: Activity, intent: Intent): Boolean {
        return true
    }

    override fun onStatusChange(
        responseData: ChangeBookingStatusResponse,
        bookingStatus: Int
    ) {

        stopLocationUpdates()


        if (bookingStatus == 3) {
            goToSignatureFragment()
        } else {
            goToHistoryFragment()
        }

    }

    fun goToSignatureFragment() {

//        view.changeFragment(10)

        var intent = Intent(this, SignatureActivity::class.java)
        intent.putExtra(Constant.BOOKING_ID, bookingId)

        startActivityForResult(intent, 101)

    }

    fun goToHistoryFragment() {
        Utility.setSharedPreference(this, "booking_complete", "booking_complete");

        DialogUtils.showCustomAlertDialog(
            this,
            " Delivery successfully completed",
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {

                    onBackPressed()


                }
            })
    }


    override fun onCallClick(receiverPhone: String) {
        if (getData != null) {
            bottomSheetDialogFragment!!.dismiss()
        }

    }

    override fun onArrivedClick() {
        bottomSheetDialogFragment!!.dismiss()
        if (getData != null) {


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(this, Constant.USER_ID).toString()
            )
            input.addProperty(
                "booking_id",
                getData!!.bookingId
            )


            input.addProperty(
                "booking_status",
                3
            )


//            goToSignatureFragment()

            presenter!!.changeBookingStatus(input, 3)
        }
    }


    override fun onArrivedAtCollection() {

        bottomSheetDialogFragment!!.dismiss()


        var input = JsonObject()
        input.addProperty(
            "userid",
            Utility.getSharedPreferences(this, Constant.USER_ID).toString()
        )
        input.addProperty(
            "booking_id",
            getData!!.bookingId
        )



        pickUpMarker!!.remove()
        dropMarker!!.remove()

        presenter!!.updateBookingStatus(input)

    }

    override fun onOpenclick() {
        bottomSheetDialogFragment!!.dismiss()
        if (getData != null) {

            var packageDetail: String

            packageDetail = Gson().toJson(getData)

            val intent = Intent(this, PackageDetailActivity::class.java)

            intent.putExtra(Constant.PACKAGE_DETAIL, packageDetail)
            intent.putExtra(Constant.FROM, 3)
            startActivity(intent)


        }
    }

    override fun onCancle() {
        bottomSheetDialogFragment!!.dismiss()
    }

    override fun onItemSelected(item: String) {
        bottomSheetDialogFragment!!.dismiss()

    }

    override fun onUpdateStatus(message: String) {


        Log.e(javaClass.simpleName, "Pankaj" + message)

        DialogUtils.showSuccessDialog(this, "Load successfully collected")


        this.bookingStatus = 6

        updatePickUpLatiLong(
            getData!!.packageDetail.senderLat.toDouble(),
            getData!!.packageDetail.senderLong.toDouble()
        )

        updateDropLatiLong(
            getData!!.packageDetail.receiverLat.toDouble(),
            getData!!.packageDetail.receiverLong.toDouble()
        )

        pickMarkerOption = MarkerOptions().position(pickupLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pickup))

        dropMarkerOption = MarkerOptions().position(dropLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_drop))

        driverMakerOption = MarkerOptions().position(driverLatLong)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.nav_arrow)).title("You")

        pickUpMarker = mMap!!.addMarker(pickMarkerOption)
        dropMarker = mMap!!.addMarker(dropMarkerOption)
        mMap!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                pickupLatLng,
                Constant.MAP_ZOOM_LEVEL
            )
        )
        drawRouteOnMap(pickupLatLng, dropLatLng)
    }

    override fun onLocationChanged(location: Location?) {
        Log.e("onLocationChanged",location.toString())
        if(driverLatLong==LatLng(0.0,0.0)||driverLatLong==null){
            presenter!!.getCurrentBooking()
        }else{
            driverLatLong = LatLng(location!!.latitude, location!!.longitude)

        }



        Log.e(
            javaClass.simpleName,
            "Latitude : " + location!!.latitude + "Longitude :" + location!!.longitude
        )

        if (location != null) {
            updateDriverLocation(location)
        }

    }

    fun updateDriverLocation(mCurrentLocation: Location) {

        updateDriverLatLong(mCurrentLocation.latitude, mCurrentLocation.longitude)

        if (updateLocation) {
            var json = JsonObject()
            json.addProperty(
                "userid",
                Utility.getSharedPreferences(this, Constant.USER_ID)
            )
            json.addProperty("latitude", mCurrentLocation.latitude)
            json.addProperty("longitude", mCurrentLocation.longitude)
            json.addProperty("booking_id", bookingId)
            presenter!!.updateDriverLatLong(json)

            if (mMap != null) {
                /**
                 * Draw on map
                 */
                if (driverLatLong != null && dropLatLng != null) {
                    drawRouteOnMap(pickupLatLng, dropLatLng)

                }

            } else {
                Toast.makeText(this, "Initializing map, Please wait...", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun updatePickUpLatiLong(latitude: Double, longitude: Double) {

        pickupLatLng = LatLng(latitude, longitude)

        Log.e(javaClass.simpleName, pickupLatLng.toString())


    }

    private fun updateDropLatiLong(latitude: Double, longitude: Double) {
        dropLatLng = LatLng(latitude, longitude)
    }

    private fun updateDriverLatLong(latitude: Double, longitude: Double) {

        driverLatLong = LatLng(latitude, longitude)

    }

    private fun onUpdateDriverMarker(newPosition: LatLng) {
        if (driverMaker == null) {

            driverMakerOption = MarkerOptions().position(driverLatLong)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nav_arrow))
            driverMaker = mMap!!.addMarker(driverMakerOption!!.position(newPosition))
        } else {
            driverMaker!!.position = newPosition
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.e(javaClass.simpleName, "onMapReady")
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isMapToolbarEnabled = false
        mMap!!.isTrafficEnabled = false
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap!!.isMyLocationEnabled = false


    }

    fun stopLocationUpdates() {

        updateLocation = false
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this
                )
                Log.e(javaClass.simpleName, "Location update stopped .......................")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        progressDialog.dismiss()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @Synchronized
    private fun drawRouteOnMap(currentLatLng: LatLng, dropLatLng: LatLng) {

        //AIzaSyDZ9gH9Lw82JuXc5RhAenu4F5adQ99FWgM
        GoogleDirection.withServerKey("AIzaSyCtRC9jkwrnxxEzY95GIBeDb0tC3knP5sM")
            .from(currentLatLng)
            .to(dropLatLng)
            .transportMode(TransportMode.DRIVING)
            .execute(this)

    }

    override fun onDirectionSuccess(direction: Direction, rawBody: String) {
        Log.e(javaClass.simpleName, "onDirectionSuccess")

        /**
         * Set marker for driver
         */
        onUpdateDriverMarker(driverLatLong)

        /**
         * Set marker for user/customer
         */
//          onUpdateUserMarker(dropLatLng)


        if (direction.isOK) {
            Log.e(javaClass.simpleName, direction.isOK.toString())
            val route = direction.routeList[0]
            val directionPositionList = route.legList[0].directionPoint

            /**
             * Drawing polyline in the Google Map for the i-th route
             */
            if (bluePolyline != null && lightBluePolyline != null) {
                bluePolyline!!.remove()
                lightBluePolyline!!.remove()

            }



            bluePolyline = mMap!!.addPolyline(
                DirectionConverter.createPolyline(
                    this,
                    directionPositionList,
                    3,
                    Color.parseColor("#2196F3")
                )
            )
            lightBluePolyline = mMap!!.addPolyline(
                DirectionConverter.createPolyline(
                    this,
                    directionPositionList,
                    3,
                    Color.parseColor("#702196F3")
                )
            )

            animatePolyLine(directionPositionList, bluePolyline!!, lightBluePolyline!!)

            if (previousLatLong != null) {


                animateMarkerAndZoomCamera(
                    mMap!!,
                    driverMaker,
                    previousLatLong!!,
                    driverLatLong,
                    MAP_ZOOM_LEVEL
                )

                previousLatLong = driverLatLong


            } else {

                previousLatLong = driverLatLong
                animateMarkerAndZoomCamera(
                    mMap!!,
                    driverMaker,
                    previousLatLong!!,
                    driverLatLong,
                    MAP_ZOOM_LEVEL
                )
            }

        } else {
            //            Log.view(TAG, direction.getErrorMessage());
            Log.e(javaClass.simpleName, "Direction can not be found!")
        }

    }

    override fun onDirectionFailure(t: Throwable) {
        Log.e(javaClass.simpleName, "onDirectionSuccess : " + t.localizedMessage!!)

    }

    override fun onConnected(p0: Bundle?) {

        Log.e(javaClass.simpleName, "Connected")


        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

        createLocationRequest()
        startLocationUpdates()
//        presenter!!.getCurrentBooking()


    }

    override fun onConnectionFailed(p0: ConnectionResult) {

        Log.e(javaClass.simpleName, "onConnectionFailed")
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.e(javaClass.simpleName, "onConnectionSuspended : " + p0)
    }

    override fun validateError(message: String) {

        DialogUtils.showSuccessDialog(this, message)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

//        Utility.log(javaClass.simpleName, requestCode.toString())
//        Utility.log(javaClass.simpleName, resultCode.toString())
//        Utility.log(javaClass.simpleName, data!!.getData().toString())

        if (resultCode == Activity.RESULT_OK) {


            Utility.log(javaClass.simpleName, data!!.getData().toString())
            goToHistoryFragment()

            /*  DialogUtils.showCustomAlertDialog(
                  activity!!,
                  data!!.getData().toString(),
                  object : DialogUtils.CustomDialogClick {
                      override fun onOkClick() {
                          view.changeFragment(3)

                      }
                  }
              )*/
        }
    }

    fun startLocationUpdates() {
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if (!gps_enabled && !network_enabled) {
            //        if (!gps_enabled) {
            //        if (!network_enabled) {
            displayLocationSettingsRequest(this)
        } else if (!checkPermissions()) {
            requestPermission()
        } else {


            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )

            Log.e(javaClass.simpleName, "Location update started ..............: ")
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("RestrictedApi")
    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setSmallestDisplacement(SMALLEST_DISPLACEMENT.toFloat())
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }


    private fun chekLocationPermission() {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "Premission Granted")

            proceedAfterPermission()

        } else {
            Log.e(javaClass.simpleName, "Premission Not Granted")
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (!checkProfilePermissions()) {
            Log.e("Permission ", "Not Granted")


            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[2]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.profilePermissionsRequired[3]
                ))
            ) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs Location permission to access your location")
                builder.setPositiveButton(
                    "Grant"
                ) { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Constant.profilePermissionsRequired[2],
                            Constant.profilePermissionsRequired[3]
                        ),
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
                    Constant.profilePermissionsRequired[2]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs Location permission to access your location")
                builder.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
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
                    arrayOf(
                        Constant.profilePermissionsRequired[2],
                        Constant.profilePermissionsRequired[3]
                    ),
                    Constant.PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                this,
                Constant.profilePermissionsRequired[2],
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
                        Constant.profilePermissionsRequired[2]
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Constant.profilePermissionsRequired[3]
                    )
                ) {

                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Permission request_old")
                    builder.setMessage("This app needs Location permission to access your location")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()


                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Constant.profilePermissionsRequired[2],
                                Constant.profilePermissionsRequired[3]
                            ),
                            Constant.PROFILE_PERMISSION_CALLBACK
                        )
                    }
                    builder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    Toast.makeText(this, "Unable to get required permission", Toast.LENGTH_LONG)
                        .show();
                }
            }
        }
    }

    private fun proceedAfterPermission() {


        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()

    }

    private fun checkProfilePermissions(): Boolean {

        return (ActivityCompat.checkSelfPermission(
            this,
            Constant.profilePermissionsRequired[2]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Constant.profilePermissionsRequired[3]
        ) == PackageManager.PERMISSION_GRANTED)

    }

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.smallestDisplacement = SMALLEST_DISPLACEMENT.toFloat()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())


        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> Log.e(
                    javaClass.simpleName,
                    "All location settings are satisfied."
                )
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.e(
                        javaClass.simpleName,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )

                    try {
                        status.startResolutionForResult(
                            this,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(javaClass.simpleName, "PendingIntent unable to execute request_old.")
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                    javaClass.simpleName,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }

    fun animatePolyLine(
        latLngList: ArrayList<LatLng>,
        blackPolyLine: Polyline,
        grayPolyLine: Polyline
    ) {
        var listLatLng: List<LatLng>
        var blackPolyline: Polyline?
        var grayPolyline: Polyline?
        var polyLineAnimationListener: Animator.AnimatorListener? = null
        listLatLng = latLngList
        blackPolyline = blackPolyLine
        grayPolyline = grayPolyLine

        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animator ->
            val latLngList = blackPolyline.getPoints()
            val initialPointSize = latLngList.size
            val animatedValue = animator.animatedValue as Int
            val newPoints = animatedValue * listLatLng.size / 100

            if (initialPointSize < newPoints) {
                latLngList.addAll(listLatLng.subList(initialPointSize, newPoints))
                blackPolyline.setPoints(latLngList)
            }
        }

        if (polyLineAnimationListener != null) {

            animator.addListener(polyLineAnimationListener)

        } else {

            polyLineAnimationListener = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}

                override fun onAnimationEnd(animator: Animator) {

                    val blackLatLng = blackPolyline.getPoints()
                    val greyLatLng = grayPolyline.getPoints()

                    greyLatLng.clear()
                    greyLatLng.addAll(blackLatLng)
                    blackLatLng.clear()

                    blackPolyline.setPoints(blackLatLng)
                    grayPolyline.setPoints(greyLatLng)

                    blackPolyline.setZIndex(2f)

                    animator.start()
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            }

            animator.addListener(polyLineAnimationListener)
        }

        animator.start()

    }

    fun animateMarkerAndZoomCamera(
        googleMap: GoogleMap,
        marker: Marker?,
        previousLatLng: LatLng,
        currentLatLng: LatLng?,
        MAP_ZOOM_LEVEL: Float
    ) {
        if (marker != null && currentLatLng != null && marker.position != null) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val proj = googleMap.projection
            val startPoint = proj.toScreenLocation(marker.position)
            val startLatLng = proj.fromScreenLocation(startPoint)
            val duration: Long = 1000

            val interpolator = LinearInterpolator()

            try {
                handler.post(object : Runnable {
                    override fun run() {
                        val elapsed = SystemClock.uptimeMillis() - start
                        val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                        val lng = t * currentLatLng.longitude + (1 - t) * startLatLng.longitude
                        val lat = t * currentLatLng.latitude + (1 - t) * startLatLng.latitude
                        //                    marker.setPosition(new LatLng(lat, lng));

                        val poitLat = LatLng(lat, lng)
                        bearing = bearingBetweenLocations(previousLatLng, poitLat).toFloat()
                        marker.setPosition(poitLat)
                        marker.setAnchor(0.5f, 0.5f)
                        marker.isDraggable = true

                        if (t < 1.0) {
                            /**
                             * Post again 16ms later.
                             */
                            handler.postDelayed(this, 16)
                        } else {


                            //                        marker.setRotation(bearing);
                            val cameraPosition = CameraPosition.Builder()
                                .target(currentLatLng)             // Sets the center of the map to current location
                                .zoom(MAP_ZOOM_LEVEL)                   // Sets the zoom
                                .bearing(bearing) // Sets the orientation of the camera to east
                                .tilt(45f)                   // Sets the tilt of the camera to 0 degrees
                                .build()                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    cameraPosition
                                )
                            )

                            isFirstTime = false

                        }
                    }
                })
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, e.localizedMessage)
            }



            if (isFirstTime) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        MAP_ZOOM_LEVEL
                    )
                )
            }


        }

    }

    fun bearingBetweenLocations(sourceLatLng: LatLng, destination: LatLng): Double {

        val begin = Location(LocationManager.NETWORK_PROVIDER)
        begin.latitude = sourceLatLng.latitude
        begin.longitude = sourceLatLng.longitude

        val end = Location(LocationManager.NETWORK_PROVIDER)
        end.latitude = destination.latitude
        end.longitude = destination.longitude


        val bearing = begin.bearingTo(end)
        //        bearing = (bearing > 0) ? bearing : 0;
        Log.e(javaClass.simpleName, "BEARING : $bearing")

        return bearing.toDouble()

    }


    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        presenter!!.onStop()

    }

}

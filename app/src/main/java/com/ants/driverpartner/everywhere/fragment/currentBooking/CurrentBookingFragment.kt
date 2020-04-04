package com.ants.driverpartner.everywhere.fragment.currentBooking


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.activity.base.BaseMainFragment
import com.ants.driverpartner.everywhere.activity.packageDetailActivity.PackageDetailActivity
import com.ants.driverpartner.everywhere.databinding.FragmentCurrentBookingBinding
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

class CurrentBookingFragment(private var view: Homeview) : BaseMainFragment(), OnMapReadyCallback,
    com.google.android.gms.location.LocationListener,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    DirectionCallback, Currentview, CustomInterface {


    lateinit var binding: FragmentCurrentBookingBinding
    private lateinit var mMap: GoogleMap
    private var mGoogleApiClient: GoogleApiClient? = null
    private var marker: Marker? = null
    private var marker1: Marker? = null
    private var bluePolyline: Polyline? = null
    private var lightBluePolyline: Polyline? = null
    var pickupLatLng = LatLng(28.61, 77.22)
    var dropLatLng = LatLng(28.61, 76.22)
    var mapFragment: SupportMapFragment? = null
    var bottomSheetDialogFragment: BottomSheetDialogFragment? = null
    private var presenter: CurrentPresenter? = null
    var getData: GetCurrentBookingRespone.Data? = null
    private var bookingId = 0


    private val SMALLEST_DISPLACEMENT: Long = 5
    private var mLocationRequest: LocationRequest? = null
    val PERMISSION_REQUEST_CODE = 200
    val REQUEST_CHECK_SETTINGS = 101

    private val INTERVAL = (1000 * 10).toLong()
    private val FASTEST_INTERVAL = (1000 / 2).toLong()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_current_booking, container, false)
        presenter = CurrentPresenter(this, activity!!)
        view.setHeaderTitle(getString(R.string.current_fragment))
        init()
        return binding.root

    }


    fun init() {
        presenter!!.getCurrentBooking()
    }


    override fun onGetCurrentBooking(responseData: GetCurrentBookingRespone) {

        bookingId = responseData.data[0].bookingId

        getData = responseData.data[0]

        pickupLatLng = LatLng(
            responseData.data[0].packageDetail.senderLat.toDouble(),
            responseData.data[0].packageDetail.senderLong.toDouble()
        )
        dropLatLng = LatLng(
            responseData.data[0].packageDetail.receiverLat.toDouble(),
            responseData.data[0].packageDetail.receiverLong.toDouble()
        )
        chekLocationPermission()


        binding.tvBookingInfo.setOnClickListener(View.OnClickListener {

            bottomSheetDialogFragment = BottomSheetFragment(this, responseData.data[0])
            bottomSheetDialogFragment!!.show(childFragmentManager, "Bottomsheet")
        })
    }

    override fun onStatusChange(responseData: ChangeBookingStatusResponse) {

        stopLocationUpdates()

        DialogUtils.showCustomAlertDialog(
            activity!!,
            responseData.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {

                    goToHistoryFragment()
                }
            })

    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            updateDriverLocation(location)
        }

    }

    @SuppressLint("RestrictedApi")
    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setSmallestDisplacement(SMALLEST_DISPLACEMENT.toFloat())
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    fun goToHistoryFragment() {

        view.changeFragment(3)

    }

    override fun onCallClick() {
        if (getData != null) {
            bottomSheetDialogFragment!!.dismiss()
        }

    }

    override fun onArrivedClick() {
        bottomSheetDialogFragment!!.dismiss()
        if (getData != null) {
            presenter!!.changeBookingStatus(getData!!.bookingId)
        }
    }

    override fun onOpenclick() {
        bottomSheetDialogFragment!!.dismiss()
        if (getData != null) {

            var packageDetail: String


            packageDetail = Gson().toJson(getData)

            val intent = Intent(activity!!, PackageDetailActivity::class.java)

            intent.putExtra(Constant.PACKAGE_DETAIL, packageDetail)
            startActivity(intent)

        }
    }

    override fun onCancle() {
        bottomSheetDialogFragment!!.dismiss()
    }

    override fun onItemSelected(item: String) {
        bottomSheetDialogFragment!!.dismiss()

    }

    private fun chekLocationPermission() {

        Log.e(javaClass.simpleName, "check Premission")

        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {


            Log.e(javaClass.simpleName, "Premission Granted")

            createLocationRequest()
            mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
            mGoogleApiClient!!.connect()

        } else {
            Log.e(javaClass.simpleName, "Premission Not Granted")
            requestPermission()
        }
    }

    fun startLocationUpdates() {
        val lm = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
            displayLocationSettingsRequest(activity!!.applicationContext)
        } else if (!checkPermissions()) {
            requestPermission()
        } else {


            if (ActivityCompat.checkSelfPermission(
                    activity!!.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity!!.applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request_old the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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
            ActivityCompat.checkSelfPermission(activity!!.applicationContext, ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (!checkProfilePermissions()) {
            Log.e("Permission ", "Not Granted")


            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Constant.profilePermissionsRequired[2]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Constant.profilePermissionsRequired[3]
                ))
            ) {

                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs Location permission to access your location")
                builder.setPositiveButton("Grant") { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        activity!!,
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
                    activity!!,
                    Constant.profilePermissionsRequired[2]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("Permission request_old")
                builder.setMessage("This app needs storage and camera permission for captured and save image.")
                builder.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()
                        //sentToSettings = true

                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", activity!!.getPackageName(), null)
                        intent.data = uri
                        startActivityForResult(intent, Constant.REQUEST_PERMISSION_SETTING)

                        Toast.makeText(
                            activity!!,
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
                    activity!!,
                    Constant.profilePermissionsRequired,
                    Constant.PROFILE_PERMISSION_CALLBACK
                )
            }

            Utility.setSharedPreferenceBoolean(
                activity!!,
                Constant.profilePermissionsRequired[2],
                true
            )

        } else {

            mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
            mGoogleApiClient!!.connect()

        }
    }


    private fun checkProfilePermissions(): Boolean {

        return (ActivityCompat.checkSelfPermission(
            activity!!,
            Constant.profilePermissionsRequired[2]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity!!,
            Constant.profilePermissionsRequired[3]
        ) == PackageManager.PERMISSION_GRANTED)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.e(javaClass.simpleName, "onMapReady")
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.isTrafficEnabled = false

        mMap.isMyLocationEnabled = true


        val markerOptions = MarkerOptions().position(pickupLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pickup))
            .title("Begin Current Trip Navigation")
        val markerOptions1 = MarkerOptions().position(dropLatLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_drop)).title("Drop")



        marker = mMap.addMarker(markerOptions)

        marker1 = mMap.addMarker(markerOptions1)



        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                pickupLatLng,
                Constant.MAP_ZOOM_LEVEL
            )
        )
        drawRouteOnMap(pickupLatLng, dropLatLng)


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
        //  onUpdateDriverMarker(currentLatLng)

        /**
         * Set marker for user/customer
         */
        //  onUpdateUserMarker(dropLatLng)


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



            bluePolyline = mMap.addPolyline(
                DirectionConverter.createPolyline(
                    activity!!,
                    directionPositionList,
                    3,
                    Color.parseColor("#2196F3")
                )
            )
            lightBluePolyline = mMap.addPolyline(
                DirectionConverter.createPolyline(
                    activity!!,
                    directionPositionList,
                    3,
                    Color.parseColor("#702196F3")
                )
            )
            animatePolyLine(directionPositionList, bluePolyline!!, lightBluePolyline!!)


        } else {
            //            Log.view(TAG, direction.getErrorMessage());
            Log.e(javaClass.simpleName, "Direction can not be found!")
        }

    }

    override fun onDirectionFailure(t: Throwable) {
        Log.e(javaClass.simpleName, "onDirectionSuccess : " + t.localizedMessage!!)


    }

    override fun onConnected(p0: Bundle?) {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment!!.getMapAsync(this)

        startLocationUpdates()

    }


    override fun onConnectionFailed(p0: ConnectionResult) {


    }

    override fun onConnectionSuspended(p0: Int) {

        Log.e(javaClass.simpleName, "" + p0)
    }

    override fun validateError(message: String) {

        DialogUtils.showSuccessDialog(activity!!, message)

    }

    fun animatePolyLine(latLngList: List<LatLng>, blackPolyLine: Polyline, grayPolyLine: Polyline) {
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


    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        presenter!!.onStop()

    }

    fun stopLocationUpdates() {
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

    fun updateDriverLocation(mCurrentLocation: Location) {
        var json = JsonObject()
        json.addProperty(
            "userid",
            Utility.getSharedPreferences(activity!!.applicationContext, Constant.USER_ID)
        )
        json.addProperty("latitude", mCurrentLocation.latitude)
        json.addProperty("longitude", mCurrentLocation.longitude)
        json.addProperty("booking_id", bookingId)

        presenter!!.updateDriverLatLong(json)

    }




    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
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
                            activity!!,
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

}


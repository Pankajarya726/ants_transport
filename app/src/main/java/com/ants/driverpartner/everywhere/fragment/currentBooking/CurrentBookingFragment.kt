package com.ants.driverpartner.everywhere.fragment.currentBooking


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
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
import com.ants.driverpartner.everywhere.activity.signature.SignatureActivity
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
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null

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
    private var presenter: CurrentPresenter? = null
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

        chekLocationPermission()


        binding.tvBookingInfo.setOnClickListener(View.OnClickListener {

            if (getData != null) {
                bottomSheetDialogFragment =
                    BottomSheetFragment(this, getData!!, bookingStatus)
                bottomSheetDialogFragment!!.show(childFragmentManager, "Bottomsheet")
            }

        })

//        presenter!!.getCurrentBooking()
    }


    override fun onGetCurrentBooking(responseData: GetCurrentBookingRespone) {

        Log.e(javaClass.simpleName,"onGetCurrentBooking")

        bookingId = responseData.data[0].bookingId

        getData = responseData.data[0]

        bookingStatus = responseData.data[0].bookingStatus

        if (bookingStatus == 2) {

            if (driverLatLong != null) {
                updatePickUpLatiLong(driverLatLong.latitude, driverLatLong.longitude)
            }

            updateDropLatiLong(
                responseData.data[0].packageDetail.senderLat.toDouble(),
                responseData.data[0].packageDetail.senderLong.toDouble()
            )

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


    }

    override fun onStatusChange(
        responseData: ChangeBookingStatusResponse,
        bookingStatus: Int
    ) {

        stopLocationUpdates()

        DialogUtils.showCustomAlertDialog(
            activity!!,
            responseData.message,
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {

                    if (bookingStatus == 3) {
                        goToSignatureFragment()
                    } else {
                        goToHistoryFragment()
                    }
                }
            })
    }


    override fun onUpdateStatus(message: String) {


        Log.e(javaClass.simpleName, "Pankaj" + message)

        DialogUtils.showSuccessDialog(activity!!, "Load successfully collected")


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

        Log.e(
            javaClass.simpleName,
            "Latitude : " + location!!.latitude + "Longitude :" + location!!.longitude
        )

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

    fun goToSignatureFragment() {

//        view.changeFragment(10)

        var intent = Intent(activity!!, SignatureActivity::class.java)
        intent.putExtra(Constant.BOOKING_ID, bookingId)

        startActivityForResult(intent, 101)

    }

    override fun onCallClick(receiverPhone: String) {
        if (getData != null) {
            bottomSheetDialogFragment!!.dismiss()


            if (receiverPhone != null) {
                view.doCall(receiverPhone)
            }


        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Utility.log(javaClass.simpleName, requestCode.toString())
        Utility.log(javaClass.simpleName, resultCode.toString())
        Utility.log(javaClass.simpleName, data!!.getData().toString())

        if (resultCode == RESULT_OK) {


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

    override fun onArrivedClick() {
        bottomSheetDialogFragment!!.dismiss()
        if (getData != null) {


            var input = JsonObject()
            input.addProperty(
                "userid",
                Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
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
            Utility.getSharedPreferences(context!!, Constant.USER_ID).toString()
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

            val intent = Intent(activity!!, PackageDetailActivity::class.java)

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

    private fun chekLocationPermission() {

        Log.e(javaClass.simpleName, "check Premission")

        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "Premission Granted")

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
            displayLocationSettingsRequest(activity!!)
        } else if (!checkPermissions()) {
            requestPermission()
        } else {


            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity!!,
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
            ActivityCompat.checkSelfPermission(activity!!, ACCESS_FINE_LOCATION)

        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        if (!checkProfilePermissions()) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Constant.profilePermissionsRequired[2]
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Constant.profilePermissionsRequired[3]
                ))
            ) {

                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("Permission request")
                builder.setMessage("This app needs Location permission to access your location")
                builder.setPositiveButton("Grant") { dialogInterface, i ->
                    dialogInterface.cancel()
                    ActivityCompat.requestPermissions(
                        activity!!,
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
                    activity!!,
                    Constant.profilePermissionsRequired[2]
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                //Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("Permission request")
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
                    arrayOf(
                        Constant.profilePermissionsRequired[2],
                        Constant.profilePermissionsRequired[3]
                    ),
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted) {
                    allowedLocation()

//                    presenter!!.getCurrentBooking()
                } else {
                    deniedLocation()


                    showMessageOK("Permission Denied, You cannot access location data",
                        DialogInterface.OnClickListener { dialog, which ->
                            Log.e(javaClass.simpleName, "GOTO settings")

                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", activity!!.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        })

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(ACCESS_FINE_LOCATION),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }
                    }

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showgotoSettingDialog() {
        DialogUtils.showCustomAlertDialog(activity!!,
            "Permission Denied, You cannot access location data",
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {
                    Log.e(javaClass.simpleName, "GOTO settings")

                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", activity!!.packageName, null)
                    intent.data = uri
                    startActivity(intent)


                }
            })


    }

    private fun showMessageOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", okListener)
            .create()
            .show()
    }

    fun allowedLocation() {
        Log.e(javaClass.simpleName, "allowedLocation")
    }

    fun deniedLocation() {
        Log.e(javaClass.simpleName, "deniedLocation")
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

        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isMapToolbarEnabled = false
        mMap!!.isTrafficEnabled = false
        mMap!!.isMyLocationEnabled = false


    }

    private fun updateDriverLatLong(latitude: Double, longitude: Double) {

        driverLatLong = LatLng(latitude, longitude)

    }

    private fun updatePickUpLatiLong(latitude: Double, longitude: Double) {

        pickupLatLng = LatLng(latitude, longitude)


    }

    private fun updateDropLatiLong(latitude: Double, longitude: Double) {
        dropLatLng = LatLng(latitude, longitude)
    }

    private fun onUpdateDriverMarker(newPosition: LatLng) {
        if (driverMaker == null) {

            driverMakerOption = MarkerOptions().position(driverLatLong)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nav_arrow))
            driverMaker = mMap!!.addMarker(driverMakerOption!!.position(newPosition))
        } else {
            driverMaker!!.setPosition(newPosition)
        }
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
                    activity!!,
                    directionPositionList,
                    3,
                    Color.parseColor("#2196F3")
                )
            )
            lightBluePolyline = mMap!!.addPolyline(
                DirectionConverter.createPolyline(
                    activity!!,
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


        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

        createLocationRequest()
        startLocationUpdates()

        presenter!!.getCurrentBooking()


    }

    override fun onConnectionFailed(p0: ConnectionResult) {

        Log.e(javaClass.simpleName, "onConnectionFailed")
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.e(javaClass.simpleName, "onConnectionSuspended : " + p0)
    }

    override fun validateError(message: String) {

        Log.e(javaClass.simpleName,"validateError1")
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

    fun animateMarkerAndZoomCamera(
        googleMap: GoogleMap,
        marker: Marker?,
        previousLatLng: LatLng,
        currentLatLng: LatLng?,
        MAP_ZOOM_LEVEL: Float
    ) {
        if (marker != null && currentLatLng != null) {
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

    fun updateDriverLocation(mCurrentLocation: Location) {

        updateDriverLatLong(mCurrentLocation.latitude, mCurrentLocation.longitude)

        if (updateLocation) {
            var json = JsonObject()
            json.addProperty(
                "userid",
                Utility.getSharedPreferences(activity!!, Constant.USER_ID)
            )
            json.addProperty("latitude", mCurrentLocation.latitude)
            json.addProperty("longitude", mCurrentLocation.longitude)
            json.addProperty("booking_id", bookingId)
            presenter!!.updateDriverLatLong(json)


        }
        if (mMap != null) {
            /**
             * Draw on map
             */
            if (driverLatLong != null && dropLatLng != null) {
                drawRouteOnMap(pickupLatLng, dropLatLng)

            }

        } else {
            Toast.makeText(activity!!, "Initializing map, Please wait...", Toast.LENGTH_SHORT)
                .show()
        }

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


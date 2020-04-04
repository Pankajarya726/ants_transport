//package com.ants.driverpartner.everywhere.service;
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.BitmapFactory;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.provider.Settings;
//import android.util.Log;
//import android.widget.Switch;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//
//import com.ants.driverpartner.everywhere.Constant;
//import com.ants.driverpartner.everywhere.R;
//import com.ants.driverpartner.everywhere.activity.Home.HomeActivity;
//import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse;
//import com.ants.driverpartner.everywhere.utils.Utility;
//import com.google.android.gms.location.LocationListener;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.maps.model.LatLng;
//import com.tekzee.mallortaxi.network.ApiClient;
//
//import java.util.HashMap;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Response;
//
//public class UpdateLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, LocationListener {
//
//    private static final String TAG = UpdateLocationService.class.getSimpleName();
//    private static final int NOTIFICATION_ID = 12345678;
//    public static int FOREGROUND_SERVICE = 101;
//    static int notificationID = 5555;
//    static int alert = 6666;
//    Location location;
//    Timer timer;
//    private GoogleApiClient mGoogleApiClient;
//    private Context mContext;
//    private LocationRequest locationRequest;
//    private CompositeSubscription mCompositeSubscription;
//    private LatLng currentLatLng;
//    private Location lastDistanceLocation;
//    private float currentDistanceInMeters;
//    private TimerTask timerTask;
//    LocationManager manager;
//
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().equals(Constant.ACTION.Companion.getSTARTFOREGROUND_ACTION())) {
//            Log.e(TAG, "Received Start Foreground Intent");
//            mContext = getApplicationContext();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                showGpsOnAlert();
//            }
//             manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new android.location.LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    Log.e("Location Changed to :", "Lattitude is -" + location.getLatitude() + "\t Longitude is -" + location.getLongitude());
//                }
//
//                @Override
//                public void onStatusChanged(String s, int i, Bundle bundle) {
//                    Log.e(TAG, s + "onStatusChanged " + i);
//                }
//
//
//                @Override
//                public void onProviderEnabled(String s) {
//                    Log.e(TAG, "onStatusChanged " + s);
//                    NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.cancel(alert);
//                }
//
//                @Override
//                public void onProviderDisabled(String s) {
//                    Log.e(TAG, "onStatusChanged " + s);
//                    showGpsOnAlert();
//                }
//            });
//
//            locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(5000);
//            locationRequest.setFastestInterval(1000);
//
//            Intent notificationIntent = new Intent(this, HomeActivity.class);
//            notificationIntent.setAction(Constant.ACTION.Companion.getMAIN_ACTION());
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 13, notificationIntent, 0);
//
//            NotificationManager mNotificationManager = (NotificationManager) mContext
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                NotificationChannel androidChannel = new NotificationChannel(getResources().getString(R.string.app_name),
//                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
//                // Sets whether notifications posted to this channel should display notification lights
//                androidChannel.enableLights(false);
//                androidChannel.setImportance(NotificationManager.IMPORTANCE_LOW);
//                androidChannel.enableVibration(false);
//
//                mNotificationManager.createNotificationChannel(androidChannel);
//            }
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent)
//                    .setChannelId(getResources().getString(R.string.app_name))
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true);
//
//            startForeground(FOREGROUND_SERVICE, mBuilder.build());
//
//            getLocation();
//
//            final Handler handler = new Handler(new Handler.Callback() {
//                @Override
//                public boolean handleMessage(Message msg) {
//                    getLocation();
//                    generateLocationAddress();
//                    return true;
//                }
//            });
//
//            timer = new Timer();
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    new Thread() {
//                        public void run() {
//                            Message msg = new Message();
//                            handler.sendMessage(msg);
//                        }
//                    }.start();
//                }
//            };
//            timer.schedule(timerTask, 0, 5000);
//        } else if (intent.getAction().equals(Constant.ACTION.Companion.getSTOPFOREGROUND_ACTION())) {
//            Log.e(TAG, "Received Stop Foreground Intent");
//            timer.cancel();
//            stopForeground(true);
//            stopSelf();
//        }
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//
//        Intent intent = new Intent(getApplicationContext(), UpdateLocationService.class)
//                .setAction(Constant.ACTION.Companion.getSTOPFOREGROUND_ACTION());
//        stopService(intent);
//
//        super.onTaskRemoved(rootIntent);
//    }
//
//    @Override
//    public void onDestroy() {
//
//        stopService(new Intent(getApplicationContext(),UpdateLocationService.class));
//        Log.e(TAG, "On Distroyed fired------------+++++++++++++++-------------");
//
//
//
//
//        timer.cancel();
//        timerTask.cancel();
//        stopForeground(true);
//        stopSelf();
//
//    }
//
//    private void showGpsOnAlert() {
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        PendingIntent pIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle(getResources().getString(R.string.app_name));
//        builder.setContentText("Please enable your Location for location updates");
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setAutoCancel(false);
//        builder.setPriority(Notification.PRIORITY_HIGH);
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setContentIntent(pIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(alert, builder.build());
//    }
//
//    public void getLocation() {
//        try {
//            // mGoogleApiClient.connect();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    locationRequest, this);
//            if (location == null) {
//                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//                Log.e("Your Current Locatoin :", "Lattitude is -" + location.getLatitude() + "\t Longitude is -" + location.getLongitude());
//            } else {
//                Log.e("Your Current Locatoin :", "Lattitude is -" + location.getLatitude() + "\t Longitude is -" + location.getLongitude());
//
//            }
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    locationRequest, UpdateLocationService.this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void generateLocationAddress() {
//        try {
//            updateDriverLocation(location);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateDriverLocation(Location location) {
//        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//        if (this.lastDistanceLocation == null) {
//            this.lastDistanceLocation = location;
//            currentDistanceInMeters = 0;
//            /**
//             * Update location on server
//             *          &
//             *  Save in local database
//             */
//            updateLatLongOnServer(location);
//
//
//        }
//
//        /**
//         * Get API distance
//         */
//        String geoDistance = "50";
//
//        if (geoDistance.equalsIgnoreCase("")) {
//            geoDistance = "10";
//        }
//
//
//        /**
//         * check distance availability provided by api
//         */
//        currentDistanceInMeters += location.distanceTo(lastDistanceLocation);
//        if (currentDistanceInMeters >= Integer.parseInt(geoDistance)) {
//            /**
//             * update location on server
//             */
//            updateLatLongOnServer(location);
//
//
//            /**
//             * re-set count
//             */
//            this.lastDistanceLocation = location;
//            currentDistanceInMeters = 0;
//
//        }
//
//
//    }
//
//    private void updateLatLongOnServer(Location location) {
//
//         Disposable disposable;
//
//         HashMap<String,String> header = new HashMap<>() ;
//        header.put("api-key",Utility.INSTANCE.getSharedPreferences(getApplicationContext(), Constant.Companion.getAPI_KEY()));
//
//        RequestBody userid = RequestBody.create(MultipartBody.FORM, "");
//        RequestBody notification_id = RequestBody.create(MultipartBody.FORM, "");
//
//        disposable = ApiClient.Companion.getInstance().callAcceptBookingApi(header,userid,notification_id).subscribe(Schedulers.io())
//                .observerOn(AndroidSchedulers.mainThread())
//                .subscribe((Response<GetNewBookingResponse> res)-> (
//
//                        Log.e("","");
//
//                        , Error -> ());
//
//
//        disposable = ApiClient.Companion.getInstance().callGetNewBookingApi(header, userid,notification_id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<GetNewBookingResponse> ->
//        //                    Utility.hideDialog()
//
//        val responseCode = response.code()
//        when (responseCode) {
//            200 -> {
//                val responseData: GetNewBookingResponse? = response.body()
//                Log.e(javaClass.simpleName, response.body().toString())
//
//                if (responseData != null) {
//                    when (responseData.status) {
//                        0 -> {
//                            view.validateError(responseData.message)
//                        }
//                        1 -> {
//                            view.onGetNewBooking(responseData)
//                        }
//                        2 -> {
//                            view.validateError(responseData.message)
//                        }
//                    }
//                } else {
//                    view.validateError(context.getString(R.string.error_message))
//                }
//
//            }
//        }
//
//                }, { error ->
//                view.hideProgressbar()
////                    Utility.hideDialog()
//            view.validateError(context.getString(R.string.error_message))
//        })
//
//
//
//        ApiClient apiStores = ApiClient.Companion.getInstance();
//        if (Utility.isConnectingToInternet()) {
//
//            JSONObject input = new JSONObject();
//            try {
//                input.put("driver_id", Utility.getSharedPreferences(getApplicationContext(), Constant.DRIVER_ID));
//                input.put("booking_id", Utility.getSharedPreferences(getApplicationContext(), Constant.BOOKING_ID));
//                input.put("is_ride_started", "1");// 1 = ride started now
//                input.put("lat", location.getLatitude());
//                input.put("long", location.getLongitude());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            com.snaprides24.driver.data.utils.Log.view(TAG, "input : " + input);
//
//            String sToken = Utility.getSharedPreferences(getApplicationContext(), Constant.S_TOKEN);
//            // apiStores.getDriverLatLong(sToken, input);
//
//
//            addSubscription(apiStores.getDriverLatLong(sToken, input), new ApiCallback() {
//                @Override
//                public void onSuccess(Object model) {
//
//                    Log.view(TAG, "success full send on server");
//
//                }
//
//                @Override
//                public void onFailure(String msg) {
//                    Log.view(TAG, "failed to  send on server" + msg);
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            });
//
//
//        } else {
//            // mvpView.onAlert(R.string.internet_error);
//        }
//    }
//
//    public void addSubscription(Observable observable, Subscriber subscriber) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        mCompositeSubscription.add(observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        this.location = location;
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true); // this is the key ingredient
//
//        PendingResult <LocationSettingsResult> result = LocationServices.SettingsApi
//                .checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback <LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult result) {
//                Status status = result.getStatus();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can
//                        // initialize location requests here.
//                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
//                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                                locationRequest, UpdateLocationService.this);
//                        generateLocationAddress();
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be
//                        // fixed by showing the user a dialog.
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have
//                        // no way to fix the settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//    }
//
//}
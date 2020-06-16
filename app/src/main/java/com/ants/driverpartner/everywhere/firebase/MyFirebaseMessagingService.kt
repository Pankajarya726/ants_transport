package com.ants.driverpartner.everywhere.firebase

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.HomeActivity
import com.ants.driverpartner.everywhere.activity.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var TAG = "FirebaseMessagingService"
    val channelID = "default"
    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        if (remoteMessage.notification != null) {
            Log.e(TAG, "Dikirim dari: ${remoteMessage.data}")

            if (isAppIsInBackground(this)) {
                Log.e(TAG, "app in background: ${remoteMessage.data}")
                showNotification(
                    remoteMessage.notification?.title,
                    remoteMessage.notification?.body
                )
            }

            sendOreoNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body
            )
        }
    }

     fun sendOreoNotification(title: String?, message: String?) {
        Log.e(TAG, "sendOreoNotification dari: $message")

        createNotificationChannel()
        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ants)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val id = System.currentTimeMillis().toInt()
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }

//        val BOOKING_REQUEST = "BOOKING_REQUEST"
//        val managerCompat = NotificationManagerCompat.from(this)
//        val notification = NotificationCompat.Builder(this, BOOKING_REQUEST)
//            .setSmallIcon(R.mipmap.logo)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//            .build()
//
//        val id = System.currentTimeMillis().toInt()
//
//        managerCompat.notify(id, notification)


    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ants)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendNotification(title: String?, description: String?) {

        val uniqueID = System.currentTimeMillis().toInt()

        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(uniqueID, builder.build())

    }

    @SuppressLint("NewApi")
    internal fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
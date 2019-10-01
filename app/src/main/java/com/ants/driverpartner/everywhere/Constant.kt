package com.ants.driverpartner.everywhere

import android.Manifest

interface Constant {

    interface ACTION {
        companion object {
            val STARTFOREGROUND_ACTION = "ACTION_START_FOREGROUND_SERVICE"
            val STOPFOREGROUND_ACTION = "ACTION_STOP_FOREGROUND_SERVICE"
            val MAIN_ACTION = "ACTION_MAIN"
        }
    }

    companion object {
        val isLogEnable = true
        val PROFILE_PERMISSION_CALLBACK = 111
        val MAP_ZOOM_LEVEL = 14.0f
        val REQUEST_PERMISSION_SETTING = 333
        val profilePermissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val CURRENCY = "R"
        /**
         * Shared preference constant data
         */
        val PROVIDE_AUTHORITY = "com.ants.driverpartner.everywhere.fileprovider"
        val IS_LOGIN = "IS_LOGIN"
        val DRIVER_ID = "DRIVER_ID"
        val VEHICLE_ID = "VEHICLE_ID"
        val NAME = "NAME"
        val EMAIL = "EMAIL"
        val MOBILE = "MOBILE"
        val S_TOKEN = "S_TOKEN"
        val PLAN_ID = "PLAN_ID"
        val PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL"
        val IS_ONLINE = "IS_ONLINE"
        val BOOKING_ID = "BOOKING_ID"
        val IS_NEW_BOKKING_REQUEST = "IS_NEW_BOKKING_REQUEST"
        val IS_BOOKING_REJECTED_BY_CUSTOMER = "IS_BOOKING_REJECTED_BY_CUSTOMER"
        val GEO_DISTANCE = "GEO_DISTANCE"
    }

    interface UploadType {
        companion object {
            val ID_FRONT = "idFront"
            val ID_BACK = "idBack"
            val LICENCE_FRONT = "licenceFront"
            val LICENCE_BACK = "licenceBack"
            val INSURANCE = "insurance"
            val VEHICLE_REGISTRATION = "registration"
        }
    }

}

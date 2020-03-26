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
        val WEB_VIEW_TITLE = "WEB_VIEW_TITLE"
        val WEB_URL= "WEB_URL"
        val VEHICLE_DETAIL = "VEHICLE_DETAIL"
        val DRIVER_DETAIL = "DRIVER_DETAIL"
        val USER_ID = "USER_ID"
        val ACCOUNT_TYPE = "ACCOUNT_TYPE"

        val OWNER = "Owner"
        val DRIVER = "Driver"
        val PARTNER = "Partner"
        val BOTH = "Both"
        val PROFILE_TYPE = "Profile_type"
        val RC_SIGN_IN = 101
        val isLogEnable = true
        val PROFILE_PERMISSION_CALLBACK = 111
        val MAP_ZOOM_LEVEL = 14.0f
        val REQUEST_PERMISSION_SETTING = 333
        val profilePermissionsRequired =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        val API_KEY = "API_KEY"
        val RESIDENTIAL_ADDRESS = "RESIDENTIAL_ADDRESS"
        val POSTAL_ADDRESS = "POSTAL_ADDRESS"
        val PLAN_ID = "PLAN_ID"
        val PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL"
        val IS_ONLINE = "IS_ONLINE"
        val BOOKING_ID = "BOOKING_ID"
        val IS_NEW_BOKKING_REQUEST = "IS_NEW_BOKKING_REQUEST"
        val IS_BOOKING_REJECTED_BY_CUSTOMER = "IS_BOOKING_REJECTED_BY_CUSTOMER"
        val GEO_DISTANCE = "GEO_DISTANCE"
        val PROFILE = "profile"
    }

    interface UploadType {
        companion object {

            val USER = "USER"
            val ID_FRONT = "ID_FRONT"
            val ID_BACK = "ID_BACK"
            val LICENCE_FRONT = "LICENCE_FRONT"
            val LICENCE_BACK = "LICENCE_BACK"
            val DRIVER_FACE = "DRIVER_FACE"
            val HOME_ADDRESS = "HOME_ADDRESS"
            val BANK_LATTER = "BANK_LATTER"
            val BANK_STATEMENT = "BANK_STATEMENT"
            val OWNERSHIP = "OWNERSHIP"
            val VEHICLE_INSURANCE = "VEHICLE_INSURANCE"
            val VEHICLE_REGISTRATION = "VEHICLE_REGISTRATION"
            val VEHICLE_LICENSE = "VEHICLE_LICENSE"
            val VEHICLE_ODOMETER = "VEHICLE_ODOMETER"
            val VEHICLE_FRONT = "VEHICLE_FRONT"
            val VEHICLE_BACK = "VEHICLE_BACK"
            val VEHICLE_LEFT = "VEHICLE_LEFT"
            val VEHICLE_RIGHT = "VEHICLE_RIGHT"
        }
    }

}

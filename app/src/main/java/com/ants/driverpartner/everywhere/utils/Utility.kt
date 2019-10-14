package com.ants.driverpartner.everywhere.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utility {
    private val ANTS = "ANTS"


    fun setSharedPreference(context: Context, name: String, value: String) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.putString(name, value)
        editor.commit()
    }

    fun getSharedPreferences(context: Context, name: String): String? {
        val settings = context.getSharedPreferences(ANTS, 0)
        return settings.getString(name, "")
    }

    fun setSharedPreferenceBoolean(context: Context, name: String, value: Boolean) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.putBoolean(name, value)
        editor.commit()
    }

    fun getSharedPreferencesBoolean(context: Context, name: String): Boolean {
        val settings = context.getSharedPreferences(ANTS, 0)
        return settings.getBoolean(name, false)
    }


    fun clearSharedPreference(context: Context) {
        val settings = context.getSharedPreferences(ANTS, 0)
        val editor = settings.edit()
        editor.clear()
        editor.commit()
    }

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun PanCardValidator(pancard: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PAN_CARD_PATTERN = "^[A-Z|a-z]{5}\\d{4}[A-Z|a-z]{1}$"
        pattern = Pattern.compile(PAN_CARD_PATTERN)
        matcher = pattern.matcher(pancard)
        return matcher.matches()
    }


    fun AadharCardValidator(aadhar: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val AADHAR_PATTERN = "^[0-9]{12}$"
        pattern = Pattern.compile(AADHAR_PATTERN)
        matcher = pattern.matcher(aadhar)
        return matcher.matches()
    }

    fun VoterCardValidator(votercard: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val VOTER_PATTERN = "^[A-Z|a-z]{3}\\d{7}$"
        pattern = Pattern.compile(VOTER_PATTERN)
        matcher = pattern.matcher(votercard)
        return matcher.matches()
    }

    fun VoterCardValidator1(votercard: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val VOTER_PATTERN = "^[0-9]{5}[.]\\d[0-9]{2}$"
        pattern = Pattern.compile(VOTER_PATTERN)
        matcher = pattern.matcher(votercard)
        return matcher.matches()
    }

    fun DrivingLisncValidator(votercard: String): Boolean {
        val pattern: Pattern
        val pattern1: Pattern
        val matcher: Matcher
        val matcher1: Matcher
        val VOTER_PATTERN = "^[A-Z|a-z]{2}\\d{2}[A-Z|a-z]{1}\\d{11}$"
        val VOTER_PATTERN1 = "^[A-Z|a-z-]{2}\\d{2}[A-Z|a-z]{1}-\\d{4}-\\d{7}$"
        //var docregex1 = /^[A-Z|a-z]{2}\d{2}[A-Z|a-z]{1}\d{11}$/;
        //var docregex = /^[A-Z|a-z-]{2}\d{2}[A-Z|a-z]{1}-\d{4}-\d{7}$/;
        pattern = Pattern.compile(VOTER_PATTERN)
        pattern1 = Pattern.compile(VOTER_PATTERN1)
        matcher = pattern.matcher(votercard)
        matcher1 = pattern1.matcher(votercard)

        return matcher.matches() || matcher1.matches()
    }

    fun IfscValidator(Ifsccode: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val Ifsccode_PATTERN = "^[A-Z|a-z]{4}[0][\\d]{6}$" //^[A-Z|a-z]{4}[0][\d]{6}$
        pattern = Pattern.compile(Ifsccode_PATTERN)
        matcher = pattern.matcher(Ifsccode)
        return matcher.matches()
    }

    fun alphanumericValidator(vehicle_number: String): Boolean {
        return vehicle_number.matches("[a-zA-Z0-9]+".toRegex())
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected

    }


}

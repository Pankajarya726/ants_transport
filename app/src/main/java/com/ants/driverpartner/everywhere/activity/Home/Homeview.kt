package com.ants.driverpartner.everywhere.activity.Home

interface Homeview {
    fun setHeaderTitle(s: String)
    fun changeFragment(i: Int)
    fun logout()
    fun doCall(phoneNumber: String)
}
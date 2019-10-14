package com.ants.driverpartner.everywhere.activity.Signup

data class RegisterResponse (
    val status: Int,
    val message: String,
    val data: Data
)

data class Data (
    val name: String,
    val mobile: String,
    val email: String,
    val password: String,
    val type: String,
    val stoken: String,
    val residentialAddress: String,
    val postalAddress: String,
    val isRegistered: Long,
    val deviceToken: String,
    val accountType: String,
    val userid: String
)

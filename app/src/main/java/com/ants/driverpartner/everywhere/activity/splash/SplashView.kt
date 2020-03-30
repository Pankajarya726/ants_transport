package com.ants.driverpartner.everywhere.activity.splash

import com.ants.driverpartner.everywhere.activity.base.BaseMainView

interface SplashView:BaseMainView {
    fun OnUpdate(responseData: ValidateAppResponse)
    fun onNoUpdate()
}
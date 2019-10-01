package com.ants.driverpartner.everywhere.activity.otp

import com.ants.driverpartner.everywhere.activity.base.BasePresenter

class OtpPresenter(view: OptView) : BasePresenter<OptView>() {

    init {
        attachView(view)
    }
}
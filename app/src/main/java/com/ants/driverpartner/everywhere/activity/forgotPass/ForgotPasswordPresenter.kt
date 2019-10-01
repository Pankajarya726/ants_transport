package com.ants.driverpartner.everywhere.activity.forgotPass

import com.ants.driverpartner.everywhere.activity.base.BasePresenter

class ForgotPasswordPresenter(view: ForgotPasswordView) : BasePresenter<ForgotPasswordView>() {

    init {
        attachView(view)
    }
}
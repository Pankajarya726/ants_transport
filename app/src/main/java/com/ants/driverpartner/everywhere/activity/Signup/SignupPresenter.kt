package com.ants.driverpartner.everywhere.activity.Signup

import com.ants.driverpartner.everywhere.activity.base.BasePresenter

class SignupPresenter(view: SignupView) : BasePresenter<SignupView>() {

    init {
        attachView(view)
    }
}
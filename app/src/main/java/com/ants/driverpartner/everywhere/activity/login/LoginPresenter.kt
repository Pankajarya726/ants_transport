package com.ants.driverpartner.everywhere.activity.login

import com.ants.driverpartner.everywhere.activity.base.BasePresenter

class LoginPresenter(view: LoginView) : BasePresenter<LoginView>(){


    init {
        attachView(view)
    }

}
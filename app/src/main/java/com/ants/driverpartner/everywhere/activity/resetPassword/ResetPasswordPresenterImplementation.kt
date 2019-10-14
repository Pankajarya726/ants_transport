package com.ants.driverpartner.everywhere.activity.resetPassword

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.resetPassword.ResetPasswordPresenter.ResetPasswordView
import io.reactivex.disposables.Disposable

class ResetPasswordPresenterImplementation(private var mainView: ResetPasswordView,context: Context):ResetPasswordPresenter.ResetPasswordMainPresenter {
    override fun resetpassword() {


    }

    var context: Context? = context
    private var disposable: Disposable? =null

}
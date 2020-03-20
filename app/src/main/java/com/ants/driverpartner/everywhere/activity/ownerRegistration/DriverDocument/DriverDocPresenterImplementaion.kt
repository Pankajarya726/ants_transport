package com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument

import android.content.Context
import io.reactivex.disposables.Disposable

class DriverDocPresenterImplementaion(private var mainView: DriverDocPresenter.PartnerDocView, context: Context) : DriverDocPresenter.PartnerDocMainPresenter {
    var context: Context? = context
    private var disposable: Disposable? =null
    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}